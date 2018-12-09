package it.luciferino.trainmonitoringbot.enumeration;

public enum TipoFermata {

    F("F", "Fermata"),
    P("P", "Partenza"),
    A("A", "Arrivo");

    private String value;
    private String description;

    TipoFermata(String value, String description){
        this.value = value;
        this.description = description;
    }

    String getValue(){
        return value;
    }

    String getDescription(){
        return description;
    }

}
