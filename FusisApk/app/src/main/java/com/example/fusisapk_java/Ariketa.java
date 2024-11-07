package com.example.fusisapk_java;

public class Ariketa {

    private String izena;
    private int denbora;
    private String img;

    public Ariketa(String izena, int denbora, String img) {
        this.izena = izena;
        this.denbora = denbora;
        this.img = img;
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

}
