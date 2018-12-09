package it.luciferino.trainmonitoringbot.domain.service;

import it.luciferino.trainmonitoringbot.domain.entities.Task;
import it.luciferino.trainmonitoringbot.domain.repository.TaskRepository;
import it.luciferino.trainmonitoringbot.dto.Request;
import it.luciferino.trainmonitoringbot.dto.response.GenericDTO;
import it.luciferino.trainmonitoringbot.dto.response.internal.ErrorResponse;
import it.luciferino.trainmonitoringbot.dto.response.internal.TaskResponse;
import it.luciferino.trainmonitoringbot.dto.response.internal.TrainResponse;
import it.luciferino.trainmonitoringbot.enumeration.TipoNotifica;
import it.luciferino.trainmonitoringbot.integration.IntegrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;


@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private IntegrationService integrationService;

    @Modifying
    @Transactional
    public Mono<GenericDTO> getTask(Request request){
        return Mono.just(request)
                .flatMap(req -> integrationService.doGet(req.getTrain()))
                .map(trenitaliaAndamentoResponse -> new TrainResponse(trenitaliaAndamentoResponse, request.getStation(), TipoNotifica.AGGIORNAMENTO))
                .map(trainResponse -> {
                    Mono.just(taskRepository.findByChatIdAndStationAndTrain(request.getChatId(), request.getStation(), request.getTrain())
                            .orElse(Task.builder()
                                            .chatId(request.getChatId())
                                            .train(request.getTrain())
                                            .exactStation(trainResponse.getStazioneEsatta())
                                            .latestStation(trainResponse.getUltimaPosizione())
                                            .station(request.getStation())
                                            .stationCode(trainResponse.getIdOrigine())
                                            .ritardo(trainResponse.getRitardoAttuale())
                                            .stopNumber(trainResponse.getNumeroFermata())
                                            .build())
                            )
                            .map(task -> {
                                task.setLatestRequestDate(ZonedDateTime.now());
                                return taskRepository.save(task);
                            }).subscribe();
                    return trainResponse;
                });
    }

    @Modifying
    @Transactional
    public Mono<GenericDTO> deleteTask(Request request){
        return Mono.just(taskRepository.deleteAllByChatIdAndId(request.getChatId(), request.getTaskId()))
                .map(o -> {
                    if (o <= 0)
                        return new ErrorResponse(String.format("Nessun record trovato per chat %s e id %s", request.getChatId(), request.getTaskId()));
                    return new ErrorResponse("Cancellazione avvenuta con successo");
                });
    }

    @Transactional
    public Flux<GenericDTO> listTask(Request request){
        return Mono.just(request)
                .map(req -> taskRepository.findAllByChatId(request.getChatId()))
                .flatMapMany(tasks -> Flux.fromStream(tasks.stream().map(task ->
                        TaskResponse.builder()
                                .identificativoRichiesta(task.getId())
                                .nomeStazione(task.getStation())
                                .numeroTreno(task.getTrain())
                                .build()
                )));
    }


}
