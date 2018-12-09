package it.luciferino.trainmonitoringbot.dto.response.internal;

import it.luciferino.trainmonitoringbot.dto.response.GenericDTO;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TaskResponse extends GenericDTO implements Serializable {

    private static final long serialVersionUID = -3007207469154963183L;

    private Long identificativoRichiesta;
    private String numeroTreno;
    private String nomeStazione;

}
