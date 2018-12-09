package it.luciferino.trainmonitoringbot.telegram;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.luciferino.trainmonitoringbot.dto.response.GenericResponse;
import it.luciferino.trainmonitoringbot.dto.response.ResponseList;
import it.luciferino.trainmonitoringbot.properties.ConfigBean;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.support.RouterFunctionMapping;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private static Logger logger = LoggerFactory.getLogger(TelegramLongPollingBot.class);

    private WebClient webClient;
    private Gson gson;
    private ObjectMapper mapper;

    @Autowired
    private ConfigBean configBean;

    @PostConstruct
    public void init() {
        webClient = WebClient.create("http://localhost:" + configBean.getServerPort());
        gson = new GsonBuilder().setPrettyPrinting().create();
        mapper = new ObjectMapper()
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"))
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();

        try {
            WebClient.ResponseSpec responseSpec = webClient.get()
                    .uri(prepareUri(message.getChatId(), message.getText()))
                    .retrieve();
            String prettyResponse;
            try {
                prettyResponse = gson.toJson(responseSpec.bodyToMono(GenericResponse.class).block());
            } catch (Exception ex) {
                prettyResponse = gson.toJson(new ResponseList(responseSpec
                        .bodyToFlux(GenericResponse.class)
                        .toStream()
                        .map(GenericResponse::getData)
                        .collect(Collectors.toList())).getDetailList());
        }

            sendMessage(prettyResponse, message.getMessageId(), message.getChatId());
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            sendMessage(e.getMessage(), message.getMessageId(), message.getChatId());
        }
    }

    public void sendMessage(String message, Integer messageId, Long chatId) {
        try {
            execute(new SendMessage()
                    .setText(message)
                    .setReplyToMessageId(messageId)
                    .setChatId(chatId));
        } catch (TelegramApiException e) {
            logger.error("Send message fail: {}", e.getMessage(), e);
        }
    }

    public void sendMessage(String message, Long chatId) {
        try {
            execute(new SendMessage()
                    .setText(message)
                    .setChatId(chatId));
        } catch (TelegramApiException e) {
            logger.error("Send message fail: {}", e.getMessage(), e);
        }
    }

    @Override
    public String getBotUsername() {
        return configBean.getTelegramBotUsername();
    }

    @Override
    public String getBotToken() {
        return configBean.getTelegramBotToken();
    }

    private String prepareUri(Long chatId, String endPoint) {
        return (configBean.getSpringWebservicesPath() + "/" + chatId.toString() + endPoint).replace(" ", "/");
    }

}

@Data
@Component
class EndpointsListener implements ApplicationListener {

    private List<String> supportedEndPoint;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
            applicationContext.getBean(RouterFunctionMapping.class).getRouterFunction();

            supportedEndPoint = Arrays.stream(Objects.requireNonNull(applicationContext.getBean(RouterFunctionMapping.class)
                    .getRouterFunction())
                    .toString().split("\n"))
                    .map(message -> message.split("&& ")[1].split("\\)")[0])
                    .filter(endpoint -> !endpoint.contains("{"))
                    .collect(Collectors.toList());
        }
    }
}