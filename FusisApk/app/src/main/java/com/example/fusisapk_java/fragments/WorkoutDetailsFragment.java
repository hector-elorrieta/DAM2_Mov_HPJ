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
    private Spinner spinnerMaila;  // Reemplazamos EditText por Spinner
    private Button btnSave, btnDelete, btnBack;

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

        etName = view.findViewById(R.id.etName);
        etLink = view.findViewById(R.id.Link);
        spinnerMaila = view.findViewById(R.id.spinnerMaila);  // Inicializamos el Spinner
        btnSave = view.findViewById(R.id.btnSave);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnBack = view.findViewById(R.id.btnBack);

        loadWorkoutData();
        setupSpinner();
        setupButtons();

        return view;
    }

    // Método para cargar los datos del Workout en los campos
    private void loadWorkoutData() {
        if (workout != null) {
            etName.setText(workout.getIzena());
            etLink.setText(String.valueOf(workout.getLink()));

            // Aquí configuramos el Spinner con el valor actual del nivel
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    getContext(),
                    R.array.maila_options,  // Asegúrate de tener esta lista en strings.xml
                    android.R.layout.simple_spinner_item
            );
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMaila.setAdapter(adapter);

            // Establecer el valor del nivel en el Spinner
            int position = adapter.getPosition(workout.getMaila());  // Busca la posición de `workout.getMaila()` en el array
            spinnerMaila.setSelection(position);
        }
    }

    // Configura las opciones del Spinner
    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.maila_options,  // Asegúrate de que esta lista esté definida en strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaila.setAdapter(adapter);
    }

    // Configura los eventos de los botones
    private void setupButtons() {
        btnSave.setOnClickListener(v -> {
            if (etName.getText().toString().isEmpty() || etLink.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Actualiza los valores del Workout
                workout.setIzena(etName.getText().toString());
                workout.setMaila(spinnerMaila.getSelectedItem().toString());  // Obtiene el nivel desde el Spinner
                workout.setLink(etLink.getText().toString());

                DBFuntzioak dbFuntzioak = new DBFuntzioak(getContext());
                dbFuntzioak.updateWorkout(workout.getIzena(), workout, success -> {
                    if (success) {
                        Toast.makeText(getContext(), "Workout actualizado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error al actualizar el workout", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "La duración debe ser un número válido", Toast.LENGTH_SHORT).show();
            }
        });

        btnDelete.setOnClickListener(v -> {
            if (workout.getIzena() == null || workout.getIzena().isEmpty()) {
                Toast.makeText(getContext(), "Izena de workout no válido", Toast.LENGTH_SHORT).show();
                return;
            }

            new AlertDialog.Builder(getContext())
                    .setTitle("Eliminar Workout")
                    .setMessage("¿Estás seguro de que deseas eliminar este workout?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        DBFuntzioak dbFuntzioak = new DBFuntzioak(getContext());
                        dbFuntzioak.removeWorkout(workout.getIzena(), success -> {
                            if (success) {
                                Toast.makeText(getContext(), "Workout eliminado correctamente", Toast.LENGTH_SHORT).show();
                                getParentFragmentManager().popBackStack();
                            } else {
                                Toast.makeText(getContext(), "Error al eliminar el workout", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }
}
