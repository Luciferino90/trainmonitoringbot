package it.luciferino.trainmonitoringbot.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request implements Serializable {

    private static final long serialVersionUID = 3965945483751086390L;

    private Long taskId;
    private Long chatId;
    private String station;
    private String train;

}
