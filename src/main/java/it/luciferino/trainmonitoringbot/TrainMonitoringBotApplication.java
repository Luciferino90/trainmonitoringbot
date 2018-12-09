package it.luciferino.trainmonitoringbot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.ApiContextInitializer;
import reactor.core.publisher.Hooks;

/**
 *
 * @author luciferino90
 */
@SpringBootApplication
public class TrainMonitoringBotApplication {

	public static void main(String[] args) {
		Hooks.onOperatorDebug();
		ApiContextInitializer.init();
		SpringApplication.run(TrainMonitoringBotApplication.class, args);
	}
}