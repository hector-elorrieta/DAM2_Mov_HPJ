package com.example.fusisapk_java.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fusisapk_java.DBFuntzioak;
import com.example.fusisapk_java.R;
import com.example.fusisapk_java.Workout;

public class WorkoutDetailsFragment extends Fragment {

    private static final String ARG_WORKOUT = "workout";

    private Workout workout;
    private EditText etName, etLink;
    private Spinner spinnerMaila;
    private Button btnGorde, btnEzabatu, btnAtzera;

    public static WorkoutDetailsFragment newInstance(Workout workout) {
        WorkoutDetailsFragment fragment = new WorkoutDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_WORKOUT, workout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            workout = (Workout) getArguments().getSerializable(ARG_WORKOUT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_details, container, false);

        etName = view.findViewById(R.id.etIzena);
        etLink = view.findViewById(R.id.Link);
        spinnerMaila = view.findViewById(R.id.spinnerMaila);
        btnGorde = view.findViewById(R.id.btnSave);
        btnEzabatu = view.findViewById(R.id.btnDelete);
        btnAtzera = view.findViewById(R.id.btnBack);

        loadWorkoutData();
        setupSpinner();
        setupButtons();

        etName.setEnabled(false);

        return view;
    }

    private void loadWorkoutData() {
        if (workout != null) {
            etName.setText(workout.getIzena());
            etLink.setText(String.valueOf(workout.getLink()));

            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    getContext(),
                    R.array.maila_options,
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMaila.setAdapter(adapter);

            int position = adapter.getPosition(workout.getMaila());
            spinnerMaila.setSelection(position);
        }
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.maila_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaila.setAdapter(adapter);
    }

    private void setupButtons() {
        btnGorde.setOnClickListener(v -> {
            if (etName.getText().toString().isEmpty() || etLink.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Datu guztiak bete behar dira",
                                                                        Toast.LENGTH_SHORT).show();
                return;
            }
                workout.setIzena(etName.getText().toString());
                workout.setMaila(spinnerMaila.getSelectedItem().toString());
                workout.setLink(etLink.getText().toString());

                DBFuntzioak dbFuntzioak = new DBFuntzioak(getContext());
                dbFuntzioak.updateWorkout(workout.getIzena(), workout, success -> {
                    if (success) {
                        Toast.makeText(getContext(), "Workout ondo eguneratu da",
                                                                        Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error workout eguneratzean",
                                                                        Toast.LENGTH_SHORT).show();
                    }
                });
        });

        btnEzabatu.setOnClickListener(v -> {
            if (workout.getIzena() == null || workout.getIzena().isEmpty()) {
                Toast.makeText(getContext(), "Workout ezin daiteke ezabatu",
                                                                        Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(getContext())
                    .setTitle("Workout ezabaketa")
                    .setMessage("Ziur zaude workout ezabatu nahi duzula?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        DBFuntzioak dbFuntzioak = new DBFuntzioak(getContext());
                        dbFuntzioak.removeWorkout(workout.getIzena(), success -> {
                            if (success) {
                                Toast.makeText(getContext(), "Workout ondo ezabatu da",
                                                                        Toast.LENGTH_SHORT).show();
                                getParentFragmentManager().popBackStack();
                            } else {
                                Toast.makeText(getContext(), "Error al eliminar el workout",
                                                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        btnAtzera.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }
}
