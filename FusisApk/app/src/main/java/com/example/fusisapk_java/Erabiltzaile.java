package com.example.fusisapk_java;

import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class Erabiltzaile {

    private String izena;
    private String abizena;
    private String mail;
    private Timestamp jaiotzeData;
    private String maila;
    private String mota;
    private String pasahitza;
    private String erabiltzailea;
    private ArrayList<Historikoa> historikoak;


    public Erabiltzaile (String mail, String pasahitza) {
        this.mail = mail;
        this.pasahitza = pasahitza;
    }


    public Erabiltzaile(String izena, String abizenak, String email, Timestamp jaiotzeData
    , String maila, String mota) {
        this.izena = izena;
        this.abizena = abizenak;
        this.mail = email;
        this.jaiotzeData = jaiotzeData;
        this.maila = maila;
        this.mota = mota;
    }

    public Erabiltzaile(String izena, String abizenak, String email, Timestamp jaiotzeData, String mota, String pasahitza, String erabiltzailea) {
        this.izena = izena;
        this.pasahitza = pasahitza;
        this.abizena = abizenak;
        this.mail = email;
        this.jaiotzeData = jaiotzeData;
        this.mota = mota;
        this.erabiltzailea = erabiltzailea;
    }

    public String getIzena() {
        return izena;
    }

    public void setIzena(String izena) {
        this.izena = izena;
    }

    public String getAbizena() {
        return abizena;
    }

    public void setAbizena(String abizena) {
        this.abizena = abizena;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Timestamp getJaiotzeData() {
        return jaiotzeData;
    }

    public void setJaiotzeData(Timestamp jaiotzeData) {
        this.jaiotzeData = jaiotzeData;
    }

    public String getMaila() {
        return maila;
    }

    public void setMaila(String maila) {
        this.maila = maila;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public String getPasahitza() {
        return pasahitza;
    }

    public void setPasahitza(String pasahitza) {
        this.pasahitza = pasahitza;
    }

    public String getErabiltzailea() {
        return erabiltzailea;
    }

    public void setErabiltzailea(String erabiltzailea) {
        this.erabiltzailea = erabiltzailea;
    }

    public ArrayList<Historikoa> getHistorikoak() {
        return historikoak;
    }
    public void setHistorikoak(ArrayList<Historikoa> historikoak) {
        this.historikoak = historikoak;
    }
}
