package it.luciferino.trainmonitoringbot.dto.response.trenitalia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrenitaliaAutocompleteResponse implements TrenitaliaResponse, Serializable {

    private static final long serialVersionUID = 7642575131167210311L;

    private String message;
    private String train;
    private String station;

    public TrenitaliaAutocompleteResponse(String message){
        this.message = message;
        decode();
    }

    private void decode(){
        String[] decodedMessage = message.replace("\n", "").split("\\|")[1].split("-");
        train = decodedMessage[0];
        station = decodedMessage[1];
    }

}
