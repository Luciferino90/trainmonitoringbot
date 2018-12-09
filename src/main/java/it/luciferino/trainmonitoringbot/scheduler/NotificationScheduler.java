package it.luciferino.trainmonitoringbot.scheduler;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.luciferino.trainmonitoringbot.domain.entities.Task;
import it.luciferino.trainmonitoringbot.domain.repository.TaskRepository;
import it.luciferino.trainmonitoringbot.dto.response.internal.TrainResponse;
import it.luciferino.trainmonitoringbot.dto.response.trenitalia.Fermate;
import it.luciferino.trainmonitoringbot.dto.response.trenitalia.TrenitaliaAndamentoResponse;
import it.luciferino.trainmonitoringbot.enumeration.TipoFermata;
import it.luciferino.trainmonitoringbot.enumeration.TipoNotifica;
import it.luciferino.trainmonitoringbot.integration.IntegrationService;
import it.luciferino.trainmonitoringbot.properties.ConfigBean;
import it.luciferino.trainmonitoringbot.telegram.TelegramBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@EnableScheduling
public class NotificationScheduler {

    private final AtomicBoolean enabled = new AtomicBoolean(false);

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private IntegrationService integrationService;

    @Autowired
    private TelegramBot telegramBot;

    @Autowired
    private ConfigBean configBean;

    private ObjectMapper mapper;

    private Gson gson;

    @PostConstruct
    private void init(){
        gson = new GsonBuilder().setPrettyPrinting().create();
        enabled.set(configBean.getSchedulerEnabled());
        mapper = new ObjectMapper()
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"))
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    public void toggle(){
        enabled.set(!enabled.get());
    }

    public Boolean isWorking(){
        return enabled.get();
    }

    @Scheduled(fixedRate = 150000)
    void execute(){
        AtomicInteger stopCounter = new AtomicInteger(0);
        if (isWorking())
            taskRepository.findAll()
            .forEach(task -> Mono.zip(Mono.just(task), integrationService.doGet(task.getTrain()))
                    .map(tup -> {
                        Task t = tup.getT2().getFermate()
                                        .stream()
                                        //.anyMatch(fermate -> TipoFermata.A.equals(fermate.getTipoFermata()) && !StringUtils.isEmpty(fermate.getArrivoReale()))
                                        .anyMatch(fermate -> isArrived(tup.getT1(), fermate, stopCounter.getAndIncrement()))
                                        ? delete(tup.getT1(), tup.getT2()) : updateCheck(tup.getT1(), tup.getT2());
                        return tup;
                        }
                    )
                    .doOnError(Throwable::printStackTrace)
                    .subscribe()
            );
    }

    private Task updateCheck(Task task, TrenitaliaAndamentoResponse trenitaliaAndamentoResponse){
        Mono.just(trenitaliaAndamentoResponse)
                .filter(t -> !t.getStazioneUltimoRilevamento().equalsIgnoreCase(task.getLatestStation()) || !t.getRitardo().equals(task.getRitardo()))
                .subscribe(t -> {
                    task.setRitardo(t.getRitardo());
                    task.setLatestStation(new TrainResponse(t, task.getStation(), TipoNotifica.AGGIORNAMENTO).getUltimaPosizione());
                    taskRepository.save(task);
                    notification(task, t);
                });
        return task;
    }

    private Task delete(Task task, TrenitaliaAndamentoResponse trenitaliaAndamentoResponse){
        taskRepository.delete(task);
        notification(task, trenitaliaAndamentoResponse);
        return task;
    }

    private void notification(Task task, TrenitaliaAndamentoResponse trenitaliaAndamentoResponse){
        try {
            String prettyResponse = gson.toJson(new TrainResponse(trenitaliaAndamentoResponse, task.getStation(), TipoNotifica.CANCELLAZIONE));
            telegramBot.sendMessage(prettyResponse, task.getChatId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Boolean isArrived(Task task, Fermate fermate, Integer counter){
            return TipoFermata.A.equals(fermate.getTipoFermata()) && !StringUtils.isEmpty(fermate.getArrivoReale())
                    || task.getStopNumber() < counter || task.getCtime().getDayOfYear() < ZonedDateTime.now().getDayOfYear();

    }

}
