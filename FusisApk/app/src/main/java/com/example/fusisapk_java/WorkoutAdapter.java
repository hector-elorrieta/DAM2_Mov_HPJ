package com.example.fusisapk_java;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.fusisapk_java.Workout;

import java.util.ArrayList;

public class WorkoutAdapter extends ArrayAdapter<Workout> {

    public WorkoutAdapter(Context context, ArrayList<Workout> workouts) {
        super(context, 0, workouts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.workout_item, parent, false);
        }



        Workout workout = getItem(position);

        TextView textWorkoutName = convertView.findViewById(R.id.textWorkoutName);
        TextView textWorkoutDuration = convertView.findViewById(R.id.textWorkoutDuration);
        TextView textWorkoutLevel = convertView.findViewById(R.id.textWorkoutLevel);

        textWorkoutName.setText(workout.getIzena());
        textWorkoutDuration.setText("Denbora: " + workout.getDenbora() + " min");
        textWorkoutLevel.setText("Maila: " + workout.getMaila());

        return convertView;
    }
}
