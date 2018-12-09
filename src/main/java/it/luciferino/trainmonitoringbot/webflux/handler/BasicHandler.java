package it.luciferino.trainmonitoringbot.webflux.handler;

import it.luciferino.trainmonitoringbot.dto.Request;
import org.springframework.web.reactive.function.server.ServerRequest;

public abstract class BasicHandler {

    protected Request readRequest(ServerRequest serverRequest) {
        return Request.builder()
                .chatId(serverRequest.pathVariables().containsKey("chatId") ? Long.parseLong(serverRequest.pathVariable("chatId")) : null)
                .station(serverRequest.pathVariables().containsKey("station") ? serverRequest.pathVariable("station") : null)
                .train(serverRequest.pathVariables().containsKey("train") ? serverRequest.pathVariable("train") : null)
                .taskId(serverRequest.pathVariables().containsKey("taskId") ? Long.parseLong(serverRequest.pathVariable("taskId")) : null)
                .build();
    }

}
