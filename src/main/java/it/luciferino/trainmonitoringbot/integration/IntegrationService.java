package it.luciferino.trainmonitoringbot.integration;

import it.luciferino.trainmonitoringbot.dto.response.trenitalia.TrenitaliaAndamentoResponse;
import it.luciferino.trainmonitoringbot.dto.response.trenitalia.TrenitaliaAutocompleteResponse;
import it.luciferino.trainmonitoringbot.properties.ConfigBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;

@Service
public class IntegrationService {

    @Autowired
    private ConfigBean configBean;

    private WebClient webClient;

    @PostConstruct
    private void init(){
        webClient = WebClient.builder()
                .baseUrl(configBean.getTrenitaliaServiceUrl())
                .build();
    }

    public Mono<TrenitaliaAndamentoResponse> doGet(String train){
        return webClient.get().uri(String.format(configBean.getTrenitaliaServiceAutocomplete(), train))
                .retrieve()
                .bodyToMono(String.class)
                .map(TrenitaliaAutocompleteResponse::new)
                .flatMap(trenitaliaAutocompleteResponse ->
                        doGet(trenitaliaAutocompleteResponse.getStation(), trenitaliaAutocompleteResponse.getTrain()))
                ;
    }

    public Mono<TrenitaliaAndamentoResponse> doGet(String station, String train){
        return webClient.get().uri(String.format(configBean.getTrenitaliaServiceUnique(), station, train))
                .retrieve()
                .bodyToMono(TrenitaliaAndamentoResponse.class);
    }

}
