package it.luciferino.trainmonitoringbot.webflux.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.luciferino.trainmonitoringbot.domain.service.TaskService;
import it.luciferino.trainmonitoringbot.dto.response.GenericResponse;
import it.luciferino.trainmonitoringbot.dto.response.internal.Message;
import it.luciferino.trainmonitoringbot.scheduler.NotificationScheduler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class KeyboardHandler extends BasicHandler {
/*
    @Autowired
    private TaskService taskService;

    @Autowired
    private NotificationScheduler notificationScheduler;

    public Mono<ServerResponse> plain(ServerRequest serverRequest){
        return Mono.just(serverRequest)
                .map(this::readRequest)
                .flatMap(req -> taskService.getTask(req))
                .defaultIfEmpty(new Message("Errore interno")).log()
                .onErrorResume(e -> Mono.just(new Message(e.getMessage())))
                .map(GenericResponse::new)
                .flatMap(response -> ServerResponse.ok().body(BodyInserters.fromObject(response)));
    }

    public Mono<ServerResponse> del(ServerRequest serverRequest){
        return Mono.just(serverRequest)
                .map(this::readRequest)
                .flatMap(req -> Mono.empty())
                .defaultIfEmpty(new Message("Errore interno")).log()
                .onErrorResume(e -> Mono.just(new Message(e.getMessage())))
                .map(GenericResponse::new)
                .flatMap(response -> ServerResponse.ok().body(BodyInserters.fromObject(response)));
    }


    public Mono<ServerResponse> help(ServerRequest serverRequest){
        return Mono.just(serverRequest)
                .map(req ->
                        "This bot could keep an eye on a train at your place.\n" +
                                "You just need to register the train you want to track, than he will notify it to you.\n" +
                                "These are the list of commands:\n" +
                                "\t-/train <station_name_arrival_or_partial> <number_of_train>: ex. /train rezzo 9555 will track train 9555 until 'Arezzo'\n" +
                                "\t\t Will return an error if train not exists, train doesn't have a stop like 'rezzo' or 'rezzo' is not unique.\n" +
                                "\t-/train list\n" +
                                "\t\t Will return a reduced list of train tracked. With an unique identifier\n" +
                                "\t-/train del <train_identifier>: ex: /train del 45\n" +
                                "\t\t Will delete a train tracking."
                ).map(Message::new)
                .map(GenericResponse::new)
                .flatMap(response -> ServerResponse.ok().body(BodyInserters.fromObject(response)));
    }
    */

}
