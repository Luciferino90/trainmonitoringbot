package it.luciferino.trainmonitoringbot.webflux.router;

import it.luciferino.trainmonitoringbot.webflux.handler.KeyboardHandler;
import it.luciferino.trainmonitoringbot.webflux.handler.RequestHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class KeyboardRouter extends BasicRouter {
    /*
    @Bean
    public RouterFunction<ServerResponse> keyboardRouting(KeyboardHandler keyboardHandler) {
        return RouterFunctions
                .route(RequestPredicates.GET(configBean.getSpringWebservicesPath() + "/{chatId}/del")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), keyboardHandler::del)
                .andRoute(RequestPredicates.GET(configBean.getSpringWebservicesPath() + "/{chatId}")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), keyboardHandler::plain)
                .andRoute(RequestPredicates.GET(configBean.getSpringWebservicesPath() + "/{chatId}/help")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), keyboardHandler::help)
                ;
    }
    */
}
