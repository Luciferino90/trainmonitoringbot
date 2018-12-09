package it.luciferino.trainmonitoringbot.webflux.router;

import it.luciferino.trainmonitoringbot.webflux.handler.RequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class RequestRouter extends BasicRouter {

    @Bean
    public RouterFunction<ServerResponse> customRouting(RequestHandler requestHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET(configBean.getSpringWebservicesPath() + "/{chatId}/del/{taskId}")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), requestHandler::del)
                .andRoute(RequestPredicates.GET(configBean.getSpringWebservicesPath() + "/{chatId}/{station}/{train}")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), requestHandler::get)
                .andRoute(RequestPredicates.GET(configBean.getSpringWebservicesPath() + "/{chatId}/list")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), requestHandler::list)
                .andRoute(RequestPredicates.GET(configBean.getSpringWebservicesPath() + "/scheduler/working")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), requestHandler::isSchedulerWorking)
                .andRoute(RequestPredicates.GET(configBean.getSpringWebservicesPath() + "/scheduler/toggle")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), requestHandler::toggleScheduler   )
                ;
    }

}
