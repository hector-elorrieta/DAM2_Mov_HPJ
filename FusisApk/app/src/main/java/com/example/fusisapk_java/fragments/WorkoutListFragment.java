package com.example.fusisapk_java.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fusisapk_java.R;
import com.example.fusisapk_java.Workout;
import com.example.fusisapk_java.DBFuntzioak;
import com.example.fusisapk_java.WorkoutAdapter;

import java.util.ArrayList;

public class WorkoutListFragment extends Fragment {

    private DBFuntzioak dbFuntzioak;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_list, container, false);
        dbFuntzioak = new DBFuntzioak(getContext());

        ListView listView = view.findViewById(R.id.listView);

        dbFuntzioak.getWorkoutList(new DBFuntzioak.OnWorkoutListLoadedCallback() {
            @Override
            public void onWorkoutListLoaded(ArrayList<Workout> workouts) {
                if (workouts != null && !workouts.isEmpty()) {
                    WorkoutAdapter workoutAdapter = new WorkoutAdapter(getContext(), workouts);
                    listView.setAdapter(workoutAdapter);
                } else {
                    Toast.makeText(getContext(), "Momentu honentan ez daude" +
                            "workout-ak", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }
}
