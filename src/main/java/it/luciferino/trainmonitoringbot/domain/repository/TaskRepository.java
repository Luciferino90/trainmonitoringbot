package it.luciferino.trainmonitoringbot.domain.repository;

import it.luciferino.trainmonitoringbot.domain.entities.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    Optional<Task> findByChatIdAndStationAndTrain(Long chatId, String station, String train);

    Optional<Task> findByChatIdAndTrain(Long chatId, String train);

    List<Task> findAllByChatId(Long chatId);

    Integer deleteAllByChatIdAndId(Long chatId, Long id);

    Integer deleteAllByChatId(Long chatId);

}
