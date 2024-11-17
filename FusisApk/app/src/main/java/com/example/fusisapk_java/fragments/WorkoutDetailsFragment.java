package com.example.fusisapk_java.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.fusisapk_java.DBFuntzioak;
import com.example.fusisapk_java.R;
import com.example.fusisapk_java.Workout;

import java.io.Serializable;

public class WorkoutDetailsFragment extends Fragment {

    // Constante para pasar el Workout como argumento
    private static final String ARG_WORKOUT = "workout";

    private Workout workout;  // Objeto Workout para mostrar y editar
    private EditText etName, etLevel, etLink;  // Campos para editar los datos
    private Button btnSave, btnDelete, btnBack;  // Botones para guardar, eliminar y regresar

    // Método estático para crear una nueva instancia del fragmento con el Workout
    public static WorkoutDetailsFragment newInstance(Workout workout) {
        WorkoutDetailsFragment fragment = new WorkoutDetailsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_WORKOUT, workout);  // Pasa el Workout como argumento
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recupera el Workout desde los argumentos
        if (getArguments() != null) {
            workout = (Workout) getArguments().getSerializable(ARG_WORKOUT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_details, container, false);

        // Inicializa los campos y botones de la interfaz
        etName = view.findViewById(R.id.etName);
        etLevel = view.findViewById(R.id.etLevel);
        etLink = view.findViewById(R.id.Link);
        btnSave = view.findViewById(R.id.btnSave);
        btnDelete = view.findViewById(R.id.btnDelete);
        btnBack = view.findViewById(R.id.btnBack);

        // Carga los datos del Workout en los campos EditText
        loadWorkoutData();

        // Configura los eventos de los botones
        setupButtons();

        return view;
    }

    // Método para cargar los datos del Workout en los campos EditText
    private void loadWorkoutData() {
        if (workout != null) {
            etName.setText(workout.getIzena());
            etLevel.setText(workout.getMaila());
            etLink.setText(String.valueOf(workout.getDenbora()));
        }
    }

    // Configura los eventos de los botones
    private void setupButtons() {
        // Botón para guardar los cambios del Workout
        btnSave.setOnClickListener(v -> {
            // Valida los campos antes de guardar
            if (etName.getText().toString().isEmpty() || etLevel.getText().toString().isEmpty() || etLink.getText().toString().isEmpty()) {
                Toast.makeText(getContext(), "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // Actualiza los valores del Workout con los datos introducidos
                workout.setIzena(etName.getText().toString());
                workout.setMaila(etLevel.getText().toString());
                workout.setBideoa((etLink.getText().toString()));

                // Llama a la base de datos para actualizar el Workout
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

        // Botón para eliminar el Workout
        btnDelete.setOnClickListener(v -> {
            if (workout.getIzena() == null || workout.getIzena().isEmpty()) {
                Toast.makeText(getContext(), "Izena de workout no válido", Toast.LENGTH_SHORT).show();
                return;
            }

            // Muestra un cuadro de diálogo para confirmar la eliminación
            new AlertDialog.Builder(getContext())
                    .setTitle("Eliminar Workout")
                    .setMessage("¿Estás seguro de que deseas eliminar este workout?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        DBFuntzioak dbFuntzioak = new DBFuntzioak(getContext());
                        dbFuntzioak.removeWorkout(workout.getIzena(), success -> {
                            if (success) {
                                Toast.makeText(getContext(), "Workout eliminado correctamente", Toast.LENGTH_SHORT).show();
                                getParentFragmentManager().popBackStack();  // Regresa al fragmento anterior
                            } else {
                                Toast.makeText(getContext(), "Error al eliminar el workout", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        // Botón para regresar al fragmento anterior
        btnBack.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }
}
