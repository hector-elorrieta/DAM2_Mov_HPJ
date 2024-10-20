package com.example.fusisapk_java;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class RegisterFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        Spinner spinnerMota = view.findViewById(R.id.spinnerMota);

        String[] motak = {"Bezeroa", "Prestatzailea"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, motak);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMota.setAdapter(adapter);

        EditText editJaiotzeData = view.findViewById(R.id.textDate);
        // Cuando el usuario hace clic en el EditText, se muestra un DatePickerDialog.
        // EL datepicker se muestra en un cuadro de diálogo emergente.
        // La interfaz de usuario del datepicker se proporciona por el sistema
        // y no se puede personalizar.
        editJaiotzeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment
                        (RegisterFragment.this);
                newFragment.show(getParentFragmentManager(), "datePicker");
            }
        });

        return view;
    }

    @Override
    // Usa el método onDateSet para mostrar la fecha seleccionada en el EditText.
    // Este método se ejecuta cuando el usuario selecciona una fecha en el DatePickerDialog.
    public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {
        EditText editJaiotzeData = getView().findViewById(R.id.textDate);
        editJaiotzeData.setText(day + "/" + (month + 1) + "/" + year);
    }
}
