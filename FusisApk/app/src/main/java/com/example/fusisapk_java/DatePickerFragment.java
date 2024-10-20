package com.example.fusisapk_java;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

// Esta clase sirve para mostrar un DatePickerDialog.
// El DatePickerDialog es un cuadro de diálogo que muestra un calendario
// y permite al usuario seleccionar una fecha.
public class DatePickerFragment extends DialogFragment {

    private DatePickerDialog.OnDateSetListener listener;

    public DatePickerFragment(DatePickerDialog.OnDateSetListener listener) {
        this.listener = listener;
    }

    // Esto sirve para crear un nuevo DatePickerDialog.
    // Se llama cuando se crea el cuadro de diálogo.
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int urtea = c.get(Calendar.YEAR);
        int hilabetea = c.get(Calendar.MONTH);
        int eguna = c.get(Calendar.DAY_OF_MONTH);

        // Crear el DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), listener,
                urtea, hilabetea, eguna);

        // Establecer la fecha máxima (hoy)
        datePickerDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        return datePickerDialog;
    }
}
