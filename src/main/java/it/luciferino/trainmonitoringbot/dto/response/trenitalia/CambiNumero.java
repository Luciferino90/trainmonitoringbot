package it.luciferino.trainmonitoringbot.dto.response.trenitalia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CambiNumero implements Serializable {

    private static final long serialVersionUID = 3379348387893826588L;

    private String nuovoNumeroTreno;
    private String stazione;

}
