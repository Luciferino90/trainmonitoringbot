package it.luciferino.trainmonitoringbot.telegram;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;

import javax.annotation.PostConstruct;

@Component
@ConditionalOnProperty(prefix = "telegram.bot", name = "token")
public class TelegramBotWrapper {

    private static Logger logger = LoggerFactory.getLogger(TelegramBotWrapper.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        TelegramBotsApi botsApi = new TelegramBotsApi();
        try {
            botsApi.registerBot(telegramBot);
            return;
        } catch (Exception e) {
            logger.error("Autowired hook failed {}", e.getMessage());
        }

        try {
            botsApi.registerBot(new TelegramBotFallback(telegramBot.getBotToken(), telegramBot));
        } catch (Exception e) {
            logger.error("Costruct hook failed {}", e.getMessage());
        }

    }
}
