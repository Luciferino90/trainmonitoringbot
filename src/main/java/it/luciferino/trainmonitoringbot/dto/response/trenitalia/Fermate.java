package it.luciferino.trainmonitoringbot.dto.response.trenitalia;

import com.fasterxml.jackson.annotation.JsonProperty;
import it.luciferino.trainmonitoringbot.enumeration.TipoFermata;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Fermate implements Serializable {

    private static final long serialVersionUID = -7849855072592674581L;

    private String orientamento;
    private String kcNumTreno;
    private String stazione;
    private String id;
    private String listaCorrispondenze;
    private Timestamp programmata;
    private String programmataZero;
    private Timestamp effettiva;
    private Integer ritardo;
    private String partenzaTeoricaZero;
    private String arrivoTeoricoZero;

    @JsonProperty("partenza_teorica")
    private Timestamp partenzaTeorica;

    @JsonProperty("arrivo_teorico")
    private Timestamp arrivoTeorico;
    private Boolean isNextChanged;
    private Timestamp partenzaReale;
    private String arrivoReale;
    private Integer ritardoPartenza;
    private Integer ritardoArrivo;
    private Integer progressivo;
    private String binarioEffettivoArrivoCodice;
    private String binarioEffettivoArrivoTipo;
    private String binarioEffettivoArrivoDescrizione;
    private String binarioProgrammatoArrivoCodice;
    private String binarioProgrammatoArrivoDescrizione;
    private String binarioEffettivoPartenzaCodice;
    private String binarioEffettivoPartenzaTipo;
    private String binarioEffettivoPartenzaDescrizione;
    private String binarioProgrammatoPartenzaCodice;
    private String binarioProgrammatoPartenzaDescrizione;
    private TipoFermata tipoFermata;
    private Boolean visualizzaPrevista;
    private Boolean nextChanged;
    private Integer nextTrattaType;
    private Integer actualFermataType;

}
