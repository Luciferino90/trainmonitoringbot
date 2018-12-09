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
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.server.support.RouterFunctionMapping;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.annotation.PostConstruct;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

    @Autowired
    private EndpointsListener endpointsListener;

    @PostConstruct
    public void init() {
        webClient = WebClient.create("http://localhost:" + configBean.getServerPort());
        gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        mapper = new ObjectMapper()
                .setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm a z"))
                .configure(SerializationFeature.INDENT_OUTPUT, true)
                .configure(MapperFeature.USE_ANNOTATIONS, false);
    }

    @Override
    public void onUpdateReceived(Update update) {
        Message message = update.getMessage();
        if (!message.getText().startsWith("/train"))
            return;
        dispatch(message);
    }

    private void dispatch(Message message){
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

    private void sendMessage(String message, Integer messageId, Long chatId) {
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
        return (configBean.getSpringWebservicesPath()
                + "/" + chatId.toString()
                + endPoint.replace(configBean.getSpringWebservicesTelegram(), "")).replace(" ", "/");
    }

    private void checkKeyboards(String request){
        final String command = request.replace(" ", "/");
        List<String> proxiedEndpoint = endpointsListener.getMappedRequest();
        System.out.println(proxiedEndpoint);
    }

}

@Data
@Component
class EndpointsListener implements ApplicationListener {

    private List<String> supportedEndPoint;
    private List<String> mappedRequest;

    @Autowired
    private ConfigBean configBean;

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof ContextRefreshedEvent) {
            ApplicationContext applicationContext = ((ContextRefreshedEvent) event).getApplicationContext();
            applicationContext.getBean(RouterFunctionMapping.class).getRouterFunction();

            supportedEndPoint = Arrays.stream(Objects.requireNonNull(applicationContext.getBean(RouterFunctionMapping.class)
                    .getRouterFunction())
                    .toString().split("\n"))
                    .map(endPoint -> endPoint.split("&& ")[1].split("\\)")[0])
                    .map(endPoint -> endPoint.replace("/{chatId}", ""))
                    .filter(endPoint -> !endPoint.contains("/scheduler"))
                    .collect(Collectors.toList());

            mappedRequest = supportedEndPoint.stream()
                    .map(endpoint -> endpoint.replace(configBean.getSpringWebservicesPath(), configBean.getSpringWebservicesTelegram()))
                    .flatMap(endpoint -> {
                        List<String> parts = Arrays.stream(endpoint.split("/"))
                                .filter(part -> !StringUtils.isEmpty(part))
                                .map(part -> part.startsWith("{") && part.endsWith("}") ? part : part )
                                .collect(Collectors.toList());
                        List<String> endpoints = new ArrayList<>();
                        for (int c = 0; c < parts.size(); c++)
                            endpoints.add("/" + String.join("/", parts.subList(0, c + 1)));
                        return endpoints.stream();
                    })
                    .distinct()
                    .sorted((a, b) -> Integer.compare(b.length(), a.length()))
                    .collect(Collectors.toList());
        }
    }
}