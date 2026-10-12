package com.example.quiz;

import java.util.List;

public class Pytanie {
    private final String trescPytania;
    private final List<String> odpowiedzi;
    private final String poprawnaOdpowiedz;
    private final int resIdObrazka;

    public Pytanie(String trescPytania, List<String> odpowiedzi, String poprawnaOdpowiedz, int resIdObrazka ) {
        this.trescPytania = trescPytania;
        this.odpowiedzi = odpowiedzi;
        this.poprawnaOdpowiedz = poprawnaOdpowiedz;
        this.resIdObrazka  = resIdObrazka ;
    }

    public String getTrescPytania() {
        return trescPytania;
    }

    public List<String> getOdpowiedzi() {
        return odpowiedzi;
    }

    public String getPoprawnaOdpowiedz() {
        return poprawnaOdpowiedz;
    }

    public int getResIdObrazka () {
        return resIdObrazka ;
    }
}
