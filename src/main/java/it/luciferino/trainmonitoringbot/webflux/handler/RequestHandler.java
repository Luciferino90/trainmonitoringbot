package it.luciferino.trainmonitoringbot.webflux.handler;

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
public class RequestHandler extends BasicHandler {

    @Autowired
    private TaskService taskService;

    @Autowired
    private NotificationScheduler notificationScheduler;

    public Mono<ServerResponse> get(ServerRequest serverRequest){
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
                .flatMap(req -> taskService.deleteTask(req))
                .defaultIfEmpty(new Message("Errore interno")).log()
                .onErrorResume(e -> Mono.just(new Message(e.getMessage())))
                .map(GenericResponse::new)
                .flatMap(response -> ServerResponse.ok().body(BodyInserters.fromObject(response)));
    }

    public Mono<ServerResponse> list(ServerRequest serverRequest){
        return ServerResponse.ok()
                .body(Mono.just(readRequest(serverRequest))
                    .flatMapMany(req -> taskService.listTask(req))
                    .defaultIfEmpty(new Message("Errore interno")).log()
                    .onErrorResume(e -> Mono.just(new Message(e.getMessage())))
                    .map(GenericResponse::new), GenericResponse.class);
    }

    public Mono<ServerResponse> toggleScheduler(ServerRequest serverRequest){
        return Mono.just(serverRequest)
                .map(req -> {
                    notificationScheduler.toggle();
                    return Message.builder().message("Scheduler up: " + notificationScheduler.isWorking()).build();
                })
                .flatMap(res -> ServerResponse.ok().body(BodyInserters.fromObject(res)));
    }

    public Mono<ServerResponse> isSchedulerWorking(ServerRequest serverRequest){
        return Mono.just(serverRequest)
                .map(req -> Message.builder().message("Scheduler up: " + notificationScheduler.isWorking()).build())
                .flatMap(res -> ServerResponse.ok().body(BodyInserters.fromObject(res)));
    }

    public Mono<ServerResponse> forceScheduler(ServerRequest serverRequest){
        return Mono.just(serverRequest)
                .map(req -> {
                    if (!notificationScheduler.isWorking()){
                        notificationScheduler.toggle();
                        notificationScheduler.execute();
                        notificationScheduler.toggle();
                    } else {
                        notificationScheduler.execute();
                    }
                    return Message.builder().message("OK").build();
                })
                .flatMap(res -> ServerResponse.ok().body(BodyInserters.fromObject(res)));
    }

}
