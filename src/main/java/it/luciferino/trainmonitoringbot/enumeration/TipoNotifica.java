package it.luciferino.trainmonitoringbot.enumeration;

public enum  TipoNotifica {

    AGGIORNAMENTO("Aggiornamento monitoraggio"),
    CANCELLAZIONE("Arrivo e fine monitoraggio");

    private String value;

    TipoNotifica(String value){
        this.value = value;
    }

}
