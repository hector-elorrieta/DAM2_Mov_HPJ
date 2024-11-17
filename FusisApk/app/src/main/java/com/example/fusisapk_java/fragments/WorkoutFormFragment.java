package com.example.fusisapk_java.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.fusisapk_java.DBFuntzioak;
import com.example.fusisapk_java.R;
import com.example.fusisapk_java.Workout;
public class WorkoutFormFragment extends Fragment {
    private EditText etIzena, etDenbora, etBideoa;
    private Spinner spinnerMaila;
    private Button btnGordeWorkout, btnAtzera;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout_form, container, false);

        // Inicializar campos
        etIzena = view.findViewById(R.id.etIzena);
        etDenbora = view.findViewById(R.id.etDenbora);
        etBideoa = view.findViewById(R.id.etBideoa);
        spinnerMaila = view.findViewById(R.id.spinnerMaila);
        btnGordeWorkout = view.findViewById(R.id.btnGordeWorkout);
        btnAtzera = view.findViewById(R.id.btnAtzera);

        // Configurar el Spinner con las opciones
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.maila_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaila.setAdapter(adapter);

        // Botón "Atzera"
        btnAtzera.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new WorkoutListFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        DBFuntzioak db = new DBFuntzioak(getContext());

        // Botón "Guardar"
        btnGordeWorkout.setOnClickListener(v -> {
            // Capturar los datos del formulario
            String izena = etIzena.getText().toString().trim();
            String denboraStr = etDenbora.getText().toString().trim();
            String bideoa = etBideoa.getText().toString().trim();
            String maila = spinnerMaila.getSelectedItem().toString(); // Obtener el valor seleccionado

            // Verificar si todos los campos están llenos
            if (izena.isEmpty() || denboraStr.isEmpty() || bideoa.isEmpty()) {
                Toast.makeText(getContext(), "Por favor, llena todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    // Convertir denbora a int
                    int denbora = Integer.parseInt(denboraStr);

                    // Crear un objeto Workout
                    Workout newWorkout = new Workout(izena, denbora, bideoa, maila);

                    // Llamar al método para agregar el workout
                    boolean isAdded = db.addWorkout(newWorkout);
                    if (isAdded) {
                        Toast.makeText(getContext(), "Workout guardado correctamente", Toast.LENGTH_SHORT).show();

                        // Volver a la lista de workouts
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, new WorkoutListFragment());
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        Toast.makeText(getContext(), "Hubo un error al guardar el workout", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getContext(), "Por favor, ingresa un número válido para la duración", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}
