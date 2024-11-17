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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.maila_options,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMaila.setAdapter(adapter);

        btnAtzera.setOnClickListener(v -> {
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new WorkoutListFragment());
            transaction.addToBackStack(null);
            transaction.commit();
        });

        DBFuntzioak db = new DBFuntzioak(getContext());

        btnGordeWorkout.setOnClickListener(v -> {
            String izena = etIzena.getText().toString().trim();
            String denboraStr = etDenbora.getText().toString().trim();
            String bideoa = etBideoa.getText().toString().trim();

            String maila = spinnerMaila.getSelectedItem().toString();

            if (maila.equals("Maila")) {
                Toast.makeText(getContext(), "Maila bat aukeratu behar da", Toast.LENGTH_SHORT).show();
                return;
            }

            if (izena.isEmpty() || denboraStr.isEmpty() || bideoa.isEmpty()) {
                Toast.makeText(getContext(), "Datu guztiak bete behar dira", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!isValidVideoUrl(bideoa)) {
                Toast.makeText(getContext(), "Bideo URL balioduna sartu behar da", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int denbora = Integer.parseInt(denboraStr);
                if (denbora <= 0) {
                    Toast.makeText(getContext(), "Denbora positiboa izan behar da", Toast.LENGTH_SHORT).show();
                    return;
                }

                Workout newWorkout = new Workout(izena, denbora, bideoa, maila);
                boolean isGordeta = db.addWorkout(newWorkout);

                if (isGordeta) {
                    Toast.makeText(getContext(), "Workout ondo sartu da", Toast.LENGTH_SHORT).show();

                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.fragment_container, new WorkoutListFragment());
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    Toast.makeText(getContext(), "Error datu-basean sartzean", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(getContext(), "Denbora zenbakia izan behar da", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private boolean isValidVideoUrl(String url) {
        return url != null && (url.startsWith("http://") || url.startsWith("https://"));
    }
}
