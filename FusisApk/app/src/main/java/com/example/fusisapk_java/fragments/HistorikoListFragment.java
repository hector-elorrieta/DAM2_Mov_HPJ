package com.example.fusisapk_java.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.example.fusisapk_java.AldagaiOrokorrak;
import com.example.fusisapk_java.MailaFiltraketa;
import com.example.fusisapk_java.R;
import com.example.fusisapk_java.Workout;
import com.example.fusisapk_java.DBFuntzioak;
import com.example.fusisapk_java.WorkoutAdapter;

import java.util.ArrayList;

public class HistorikoListFragment extends Fragment {
    DBFuntzioak dbFuntzioak = new DBFuntzioak(getContext());
    AldagaiOrokorrak aldagaiOrokorrak = new AldagaiOrokorrak();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historiko_list, container, false);

        Button btnHasierakoa = view.findViewById(R.id.btnHasierako);
        Button btnErdimailakoa = view.findViewById(R.id.btnErdikoa);
        Button btnAurreratua = view.findViewById(R.id.btnAurreratu);
        Button btnAtzera = view.findViewById(R.id.buttonAtzera);

        btnHasierakoa.setEnabled(false);
        btnErdimailakoa.setEnabled(false);
        btnAurreratua.setEnabled(false);

        ListView listView = view.findViewById(R.id.listView);

        btnAtzera.setOnClickListener(v -> {
            WorkoutFragment workoutFragment = new WorkoutFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, workoutFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        dbFuntzioak.egindakoWorkoutHistorikoan(new DBFuntzioak.OnWorkoutListLoadedCallback() {
            @Override
            public void onWorkoutListLoaded(ArrayList<Workout> workouts) {
                MailaFiltraketa filtraketa = new MailaFiltraketa(workouts);

                ArrayList<Workout> hasierakoa = filtraketa.getHasierakoa();
                ArrayList<Workout> erdimailakoa = filtraketa.getErdimailakoa();
                ArrayList<Workout> aurreratua = filtraketa.getAurreratua();

                WorkoutAdapter workoutAdapter = new WorkoutAdapter(getContext(), workouts);
                listView.setAdapter(workoutAdapter);

                for (Workout workout : workouts) {
                    if (workout.getMaila().equalsIgnoreCase("Hasierakoa")) {
                        btnHasierakoa.setEnabled(true);
                    }

                    if (workout.getMaila().equalsIgnoreCase("Erdimailakoa")) {
                        btnErdimailakoa.setEnabled(true);
                    }

                    if (workout.getMaila().equalsIgnoreCase("Aurreratua")) {
                        btnAurreratua.setEnabled(true);
                    }
                }

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

                listView.setOnItemClickListener((parent, view, position, id) -> {
                    Workout selectedWorkout = (Workout) parent.getItemAtPosition(position);

                    HistorikoDetailsFragment detailsFragment = new HistorikoDetailsFragment();

                    Bundle bundle = new Bundle();
                    bundle.putString("izena", selectedWorkout.getIzena());
                    bundle.putString("denbora", String.valueOf(selectedWorkout.getDenbora()));
                    bundle.putString("maila", selectedWorkout.getMaila());
                    bundle.putString("bideoa", selectedWorkout.getLink());
                    detailsFragment.setArguments(bundle);

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, detailsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                });
            }
        });
        return view;
    }
}

