package it.luciferino.trainmonitoringbot.webflux.router;

import it.luciferino.trainmonitoringbot.properties.ConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Configuration
public abstract class BasicRouter {

    @Autowired
    ConfigBean configBean;

}
