package it.luciferino.trainmonitoringbot.domain.entities;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;

@Entity(name = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    private String station;

    @Column(name = "exact_station")
    private String exactStation;

    @Column(name = "stop_number")
    private Integer stopNumber;

    @Column(name = "station_code")
    private String stationCode;

    private String train;

    @Column(name = "latest_station")
    private String latestStation;

    @Column(name = "latest_request_date")
    private ZonedDateTime latestRequestDate;

    private Integer ritardo;

    private ZonedDateTime ctime = ZonedDateTime.now();

}
