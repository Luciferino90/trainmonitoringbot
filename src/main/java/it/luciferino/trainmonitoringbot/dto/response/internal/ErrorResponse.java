package it.luciferino.trainmonitoringbot.dto.response.internal;

import it.luciferino.trainmonitoringbot.dto.response.GenericDTO;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ErrorResponse extends GenericDTO implements Serializable {

    private static final long serialVersionUID = 8987010282113963830L;
    private String message;

}
