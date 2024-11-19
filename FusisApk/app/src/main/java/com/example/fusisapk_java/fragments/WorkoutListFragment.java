package com.example.fusisapk_java.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.OptIn;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fusisapk_java.DBFuntzioak;
import com.example.fusisapk_java.MailaFiltraketa;
import com.example.fusisapk_java.R;
import com.example.fusisapk_java.Workout;
import com.example.fusisapk_java.WorkoutAdapter;

import java.util.ArrayList;

public class WorkoutListFragment extends Fragment {
    private DBFuntzioak dbFuntzioak;
    private ListView listView;
    private Button btnHasierakoa, btnErdimailakoa, btnAurreratua, btnAtzera, btnAddWorkout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);

        btnHasierakoa = view.findViewById(R.id.btnHasierako);
        btnErdimailakoa = view.findViewById(R.id.btnErdikoa);
        btnAurreratua = view.findViewById(R.id.btnAurreratu);
        listView = view.findViewById(R.id.listView);
        btnAtzera = view.findViewById(R.id.btnAtzera);

        dbFuntzioak = new DBFuntzioak(getContext());

        loadWorkoutList();

        btnAtzera.setOnClickListener(v -> {
            WorkoutFragment workoutFragment = new WorkoutFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, workoutFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        btnAddWorkout = view.findViewById(R.id.btnAddWorkout);
        btnAddWorkout.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new WorkoutFormFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }
    private void updateListView(ArrayList<Workout> filteredWorkouts) {
        if (filteredWorkouts == null || filteredWorkouts.isEmpty()) {
            Toast.makeText(getContext(), "No hay workouts para mostrar", Toast.LENGTH_SHORT).show();
            listView.setAdapter(null);
            return;
        }

        WorkoutAdapter workoutAdapter = new WorkoutAdapter(getContext(), filteredWorkouts);
        listView.setAdapter(workoutAdapter);
    }
    private void loadWorkoutList() {
        dbFuntzioak.getWorkoutList(new DBFuntzioak.OnWorkoutListDataLoadCallback() {
            @Override
            public void onWorkoutListDataLoaded(ArrayList<Workout> workoutList) {
                if (workoutList == null || workoutList.isEmpty()) {
                    Toast.makeText(getContext(), "Ez dira topatu workout-ak",
                                                        Toast.LENGTH_SHORT).show();
                    return;
                }

                MailaFiltraketa filtraketa = new MailaFiltraketa(workoutList);
                updateListView(workoutList);
                enableButtons(workoutList);

                btnHasierakoa.setOnClickListener(v -> updateListView(filtraketa.getHasierakoa()));
                btnErdimailakoa.setOnClickListener(v -> updateListView(filtraketa.getErdimailakoa()));
                btnAurreratua.setOnClickListener(v -> updateListView(filtraketa.getAurreratua()));

                // Workout bat aukeratzean, WorkoutDetailsFragment-era joateko
                listView.setOnItemClickListener((parent, view, position, id) -> {
                    Workout selectedWorkout = workoutList.get(position);
                    WorkoutDetailsFragment detailsFragment = WorkoutDetailsFragment.newInstance(selectedWorkout);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, detailsFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                });
            }
        });
    }


    private void enableButtons(ArrayList<Workout> workoutList) {
        for (Workout workout : workoutList) {
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
    }
}

