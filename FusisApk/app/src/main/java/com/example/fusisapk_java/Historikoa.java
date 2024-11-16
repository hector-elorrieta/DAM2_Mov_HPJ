package com.example.fusisapk_java;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Historikoa {

    private String workoutIzena;
    private Timestamp data;
    private int denbora_eginda;
    private int ehunekoa;

    public Historikoa(String workoutIzena, Timestamp data, int denbora_eginda, int ehunekoa) {
        this.workoutIzena = workoutIzena;
        this.data = data;
        this.denbora_eginda = denbora_eginda;
        this.ehunekoa = ehunekoa;
    }

    public String getWorkoutIzena() {
        return workoutIzena;
    }

    public void setWorkoutIzena(String workoutIzena) {
        this.workoutIzena = workoutIzena;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }

    public int getDenbora_eginda() {
        return denbora_eginda;
    }

    public void setDenbora_eginda(int denbora_eginda) {
        this.denbora_eginda = denbora_eginda;
    }

    public int getEhunekoa() {
        return ehunekoa;
    }

    public void setEhunekoa(int ehunekoa) {
        this.ehunekoa = ehunekoa;
    }

    @Override
    public String toString() {
        return "Historikoa{" +
                "workoutIzena='" + workoutIzena + '\'' +
                ", data=" + data +
                ", denbora_eginda=" + denbora_eginda +
                ", ehunekoa=" + ehunekoa +
                '}';
    }

}
