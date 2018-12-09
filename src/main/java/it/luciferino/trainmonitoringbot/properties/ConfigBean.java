package it.luciferino.trainmonitoringbot.properties;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class ConfigBean {

    @Value("${spring.webservices.path}")
    private String springWebservicesPath;

    @Value("${trenitalia.service.url}")
    private String trenitaliaServiceUrl;

    @Value("${trenitalia.service.autocomplete}")
    private String trenitaliaServiceAutocomplete;

    @Value("${trenitalia.service.unique}")
    private String trenitaliaServiceUnique;

    @Value("${scheduler.enabled}")
    private Boolean schedulerEnabled;

    @Value("${telegram.bot.token}")
    public String telegramBotToken;

    @Value("${telegram.bot.username}")
    public String telegramBotUsername;

    @Value("${server.port}")
    public Integer serverPort;

}
