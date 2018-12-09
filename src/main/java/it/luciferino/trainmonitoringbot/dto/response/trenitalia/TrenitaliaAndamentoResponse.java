package it.luciferino.trainmonitoringbot.dto.response.trenitalia;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrenitaliaAndamentoResponse implements TrenitaliaResponse, Serializable {

    private static final long serialVersionUID = 3379348387893826588L;

    private String tipoTreno;
    private String orientamento;
    private String codiceCLiente;
    private String fermateSoppresse;
    private Timestamp dataPartenza;

    private List<Fermate> fermate;

    private String anormalita;
    private String provvedimenti;
    private String segnalazioni;
    private Timestamp oraUltimoRilevamento;
    private String stazioneUltimoRilevamento;
    private String idDestinazione;
    private String idOrigine;
    private List<CambiNumero> cambiNumero;
    private Boolean hasProvvedimenti;
    private List<String> descOrientamento;
    private String compOraUltimoRilevamento;
    private String motivoRitardoPrevalente;
    private String descrizioneVCO;
    private Integer numeroTreno;
    private String categoria;
    private String categoriaDescrizione;
    private String origine;
    private String codOrigine;
    private String destinazione;
    private String codDestinazione;
    private String origineEstera;
    private String destinazioneEstera;
    private String oraPartenzaEstera;
    private String oraArrivoEstera;
    private Integer tratta;
    private Integer regione;
    private String origineZero;
    private String destinazioneZero;
    private Timestamp orarioPartenzaZero;
    private Timestamp orarioArrivoZero;
    private Boolean circolante;
    private String binarioEffettivoArrivoCodice;
    private String binarioEffettivoArrivoDescrizione;
    private String binarioEffettivoArrivoTipo;
    private String binarioProgrammatoArrivoCodice;
    private String binarioProgrammatoArrivoDescrizione;
    private String binarioEffettivoPartenzaCodice;
    private String binarioEffettivoPartenzaDescrizione;
    private String binarioEffettivoPartenzaTipo;
    private String binarioProgrammatoPartenzaCodice;
    private String binarioProgrammatoPartenzaDescrizione;
    private String subTitle;
    private String esisteCorsaZero;
    private Boolean inStazione;
    private Boolean haCambiNumero;
    private Boolean nonPartito;
    private Integer provvedimento;
    private String riprogrammazione;
    private Timestamp orarioPartenza;
    private Timestamp orarioArrivo;
    private String stazionePartenza;
    private String stazioneArrivo;
    private String statoTreno;
    private String corrispondenze;
    private List<String> servizi;
    private Integer ritardo;
    private Integer tipoProdotto;
    private String compOrarioPartenzaZeroEffettivo;
    private String compOrarioArrivoZeroEffettivo;
    private String compOrarioPartenzaZero;
    private String compOrarioArrivoZero;
    private String compOrarioArrivo;
    private String compOrarioPartenza;
    private String compNumeroTreno;
    private List<String> compOrientamento;
    private String compTipologiaTreno;
    private String compClassRitardoTxt;
    private String compClassRitardoLine;
    private String compImgRitardo2;
    private String compImgRitardo;
    private List<String> compRitardo;
    private List<String> compRitardoAndamento;
    private List<String> compInStazionePartenza;
    private List<String> compInStazioneArrivo;
    private String compOrarioEffettivoArrivo;
    private String compDurata;
    private String compImgCambiNumerazione;

    @JsonIgnore
    private String matchedStation;


}
