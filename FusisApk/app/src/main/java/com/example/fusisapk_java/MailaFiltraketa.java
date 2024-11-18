package com.example.fusisapk_java;

import java.util.ArrayList;

public class MailaFiltraketa {

    private ArrayList<Workout> hasierakoa;
    private ArrayList<Workout> erdimailakoa;
    private ArrayList<Workout> aurreratua;

    public MailaFiltraketa(ArrayList<Workout> workouts) {
        hasierakoa = new ArrayList<>();
        erdimailakoa = new ArrayList<>();
        aurreratua = new ArrayList<>();

        for (Workout workout : workouts) {
            String workoutMaila = workout.getMaila();

            if (workoutMaila.equalsIgnoreCase("Hasierakoa")) {
                hasierakoa.add(workout);
            } else if (workoutMaila.equalsIgnoreCase("Erdimailakoa")) {
                erdimailakoa.add(workout);
            } else if (workoutMaila.equalsIgnoreCase("Aurreratua")) {
                aurreratua.add(workout);
            }
        }
    }

    public ArrayList<Workout> getHasierakoa() {
        return hasierakoa;
    }

    public ArrayList<Workout> getErdimailakoa() {
        return erdimailakoa;
    }

    public ArrayList<Workout> getAurreratua() {
        return aurreratua;
    }

}
