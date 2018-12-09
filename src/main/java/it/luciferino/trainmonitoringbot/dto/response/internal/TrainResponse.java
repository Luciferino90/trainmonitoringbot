package it.luciferino.trainmonitoringbot.dto.response.internal;

import it.luciferino.trainmonitoringbot.dto.response.GenericDTO;
import it.luciferino.trainmonitoringbot.dto.response.trenitalia.Fermate;
import it.luciferino.trainmonitoringbot.dto.response.trenitalia.TrenitaliaAndamentoResponse;
import it.luciferino.trainmonitoringbot.enumeration.TipoNotifica;
import lombok.*;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TrainResponse extends GenericDTO implements Serializable {

    private static final long serialVersionUID = 332536047717792779L;

    private TipoNotifica tipoNotifica;
    private Integer numeroTreno;
    private String stazioneRichiesta;
    private String stazioneEsatta;
    private String ultimaPosizione;
    private String ultimaRilevazione;
    private Timestamp arrivoProgrammato;
    private Integer ritardoAttuale;
    private Integer numeroFermata;
    private Timestamp arrivoPrevisto;
    private String idOrigine;

    public TrainResponse(TrenitaliaAndamentoResponse trenitaliaAndamentoResponse, String stazioneDiArrivo, TipoNotifica tipoNotifica){
        this.tipoNotifica = tipoNotifica;
        stazioneDiArrivo = stazioneDiArrivo.toLowerCase().replace(" ", "").replace(".", "");
        numeroTreno = trenitaliaAndamentoResponse.getNumeroTreno();
        ultimaPosizione = trenitaliaAndamentoResponse.getStazioneUltimoRilevamento();
        ultimaRilevazione = trenitaliaAndamentoResponse.getCompOraUltimoRilevamento();
        String finalStazioneDiArrivo = stazioneDiArrivo;
        Fermate fermata = trenitaliaAndamentoResponse.getFermate()
                .stream()
                .filter(fermate -> fermate.getStazione().toLowerCase().replace(" ", "").replace(".", "").contains(finalStazioneDiArrivo))
                .findFirst().orElseThrow(() -> new RuntimeException("Fermata non valida: " +
                        trenitaliaAndamentoResponse
                                .getFermate()
                                .stream()
                                .map(Fermate::getStazione)
                                .collect(Collectors.joining(","))
                ));
        AtomicBoolean found = new AtomicBoolean(false);
        AtomicInteger stopCounter = new AtomicInteger(0);
        trenitaliaAndamentoResponse.getFermate().forEach( fermate -> {
            if (!found.get())
                stopCounter.incrementAndGet();
            if (fermate.getStazione().toLowerCase().replace(" ", "").replace(".", "").contains(finalStazioneDiArrivo))
                found.set(true);
        });
        numeroFermata = stopCounter.get();
        stazioneRichiesta = stazioneDiArrivo;
        stazioneEsatta = fermata.getStazione();
        arrivoProgrammato = fermata.getProgrammata();
        ritardoAttuale = trenitaliaAndamentoResponse.getRitardo();
        arrivoPrevisto = fermata.getArrivoTeorico();
        idOrigine = trenitaliaAndamentoResponse.getIdOrigine();
    }

}
