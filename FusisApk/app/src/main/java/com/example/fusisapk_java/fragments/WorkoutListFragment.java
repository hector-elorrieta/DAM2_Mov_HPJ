package com.example.fusisapk_java.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.fusisapk_java.AldagaiOrokorrak;
import com.example.fusisapk_java.Erabiltzaile;
import com.example.fusisapk_java.MailaFiltraketa;
import com.example.fusisapk_java.R;
import com.example.fusisapk_java.Workout;
import com.example.fusisapk_java.DBFuntzioak;
import com.example.fusisapk_java.WorkoutAdapter;

import java.util.ArrayList;

public class WorkoutListFragment extends Fragment {
    DBFuntzioak dbFuntzioak = new DBFuntzioak(getContext());
    AldagaiOrokorrak aldagaiOrokorrak = new AldagaiOrokorrak();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);

        Button btnHasierakoa = view.findViewById(R.id.btnHasierako);
        Button btnErdimailakoa = view.findViewById(R.id.btnErdikoa);
        Button btnAurreratua = view.findViewById(R.id.btnAurreratu);

        Erabiltzaile logueatuta = aldagaiOrokorrak.erabiltzaileLogueatuta;

        ListView listView = view.findViewById(R.id.listView);

        Log.e("Maila", logueatuta.getMaila());

        if (logueatuta.getMaila().equalsIgnoreCase("Hasierakoa")) {
            btnErdimailakoa.setEnabled(false);
            btnAurreratua.setEnabled(false);

        }else if (logueatuta.getMaila().equalsIgnoreCase("Erdimailakoa")) {
            btnAurreratua.setEnabled(false);
            Log.e("Maila", logueatuta.getMaila());

        }

        dbFuntzioak.getWorkoutList(new DBFuntzioak.OnWorkoutListLoadedCallback() {
            @Override
            public void onWorkoutListLoaded(ArrayList<Workout> workouts) {
                MailaFiltraketa filtraketa = new MailaFiltraketa(workouts);

                ArrayList<Workout> hasierakoa = filtraketa.getHasierakoa();
                ArrayList<Workout> erdimailakoa = filtraketa.getErdimailakoa();
                ArrayList<Workout> aurreratua = filtraketa.getAurreratua();

                WorkoutAdapter workoutAdapter = new WorkoutAdapter(getContext(), hasierakoa);
                listView.setAdapter(workoutAdapter);

                btnHasierakoa.setOnClickListener(v -> {
                    WorkoutAdapter workoutAdapterHasierakoa = new WorkoutAdapter(getContext(), hasierakoa);
                    listView.setAdapter(workoutAdapterHasierakoa);
                });

                btnErdimailakoa.setOnClickListener(v -> {
                    WorkoutAdapter workoutAdapterErdimailakoa = new WorkoutAdapter(getContext(), erdimailakoa);
                    listView.setAdapter(workoutAdapterErdimailakoa);
                });

                btnAurreratua.setOnClickListener(v -> {
                    WorkoutAdapter workoutAdapterAurreratua = new WorkoutAdapter(getContext(), aurreratua);
                    listView.setAdapter(workoutAdapterAurreratua);
                });
            }
        });
        return view;
    }
}

