package it.luciferino.trainmonitoringbot.webflux.handler;

import it.luciferino.trainmonitoringbot.domain.service.TaskService;
import it.luciferino.trainmonitoringbot.dto.response.GenericResponse;
import it.luciferino.trainmonitoringbot.dto.response.internal.ErrorResponse;
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
                .defaultIfEmpty(new ErrorResponse("Errore interno")).log()
                .onErrorResume(e -> Mono.just(new ErrorResponse(e.getMessage())))
                .map(GenericResponse::new)
                .flatMap(response -> ServerResponse.ok().body(BodyInserters.fromObject(response)));
    }

    public Mono<ServerResponse> del(ServerRequest serverRequest){
        return Mono.just(serverRequest)
                .map(this::readRequest)
                .flatMap(req -> taskService.deleteTask(req))
                .defaultIfEmpty(new ErrorResponse("Errore interno")).log()
                .onErrorResume(e -> Mono.just(new ErrorResponse(e.getMessage())))
                .map(GenericResponse::new)
                .flatMap(response -> ServerResponse.ok().body(BodyInserters.fromObject(response)));
    }

    public Mono<ServerResponse> list(ServerRequest serverRequest){
        return ServerResponse.ok()
                .body(Mono.just(readRequest(serverRequest))
                    .flatMapMany(req -> taskService.listTask(req))
                    .defaultIfEmpty(new ErrorResponse("Errore interno")).log()
                    .onErrorResume(e -> Mono.just(new ErrorResponse(e.getMessage())))
                    .map(GenericResponse::new), GenericResponse.class);
        /*return Mono.just(serverRequest)
                .map(this::readRequest)
                .flatMapMany(req -> taskService.listTask(req))
                    .defaultIfEmpty(new ErrorResponse("Errore interno")).log()
                .onErrorResume(e -> Mono.just(new ErrorResponse(e.getMessage())))
                .map(GenericResponse::new)
                //.collectList()
                //.map( os -> new ResponseList(os.stream().map(o -> (GenericDTO)o).collect(Collectors.toList())))
                //.map(GenericResponse::new)
                .flatMap(response -> ServerResponse.ok().body(response, GenericResponse.class));*/
    }

    public Mono<ServerResponse> toggleScheduler(ServerRequest serverRequest){
        return Mono.just(serverRequest)
                .map(req -> {
                    notificationScheduler.toggle();
                    return ErrorResponse.builder().message("Scheduler up: " + notificationScheduler.isWorking()).build();
                })
                .flatMap(res -> ServerResponse.ok().body(BodyInserters.fromObject(res)));
    }

    public Mono<ServerResponse> isSchedulerWorking(ServerRequest serverRequest){
        return Mono.just(serverRequest)
                .map(req -> ErrorResponse.builder().message("Scheduler up: " + notificationScheduler.isWorking()).build())
                .flatMap(res -> ServerResponse.ok().body(BodyInserters.fromObject(res)));
    }

}
