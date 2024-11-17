package com.example.fusisapk_java;

import java.io.Serializable;

public class Workout implements Serializable {

    private String izena;
    private int denbora;
    private String link;
    private String maila;

    public Workout(String izena, int denbora, String bideoa, String maila) {
        this.izena = izena;
        this.denbora = denbora;
        this.link = bideoa;
        this.maila = maila;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public int getDenbora() {
        return denbora;
    }

    public void setDenbora(int denbora) {
        this.denbora = denbora;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getMaila() {
        return maila;
    }

    public void setMaila(String maila) {
        this.maila = maila;
    }
}
