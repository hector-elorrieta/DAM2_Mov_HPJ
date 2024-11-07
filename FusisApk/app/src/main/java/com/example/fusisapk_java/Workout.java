package com.example.fusisapk_java;

import java.util.ArrayList;

public class Workout {

    private String izena;
    private int denbora;
    private String bideoa;
    private String maila;
    private ArrayList<Ariketa> ariketak;

    public Workout(String izena, int denbora, String bideoa, String maila, ArrayList<Ariketa> ariketak) {
        this.izena = izena;
        this.denbora = denbora;
        this.bideoa = bideoa;
        this.maila = maila;
        this.ariketak = ariketak;
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

    public String getBideoa() {
        return bideoa;
    }

    public void setBideoa(String bideoa) {
        this.bideoa = bideoa;
    }

    public String getMaila() {
        return maila;
    }

    public void setMaila(String maila) {
        this.maila = maila;
    }

    public ArrayList<Ariketa> getAriketak() {
        return ariketak;
    }

    public void setAriketak(ArrayList<Ariketa> ariketak) {
        this.ariketak = ariketak;
    }

    public void addAriketa(Ariketa ariketa) {
        ariketak.add(ariketa);
    }

}
