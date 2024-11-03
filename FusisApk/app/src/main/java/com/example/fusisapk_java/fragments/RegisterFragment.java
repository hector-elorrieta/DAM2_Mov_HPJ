package com.example.fusisapk_java.fragments;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fusisapk_java.DataFuntzioak;
import com.example.fusisapk_java.DatePickerFragment;
import com.example.fusisapk_java.Erabiltzaile;
import com.example.fusisapk_java.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import com.example.fusisapk_java.DBFuntzioak;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DBFuntzioak dbFuntzioak;

    private EditText txtIzena, txtAbizenak, txtErabiltzaile, txtPasahitza, editJaiotzeData,
            textEmail;
    private Spinner spinnerMota;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        txtIzena = view.findViewById(R.id.textIzena);
        txtAbizenak = view.findViewById(R.id.textAbizena);
        textEmail = view.findViewById(R.id.textEmail);
        txtErabiltzaile = view.findViewById(R.id.textErabiltzailea);
        txtPasahitza = view.findViewById(R.id.textPasahitza);
        spinnerMota = view.findViewById(R.id.spinnerMota);

        Spinner spinnerMota = view.findViewById(R.id.spinnerMota);
        Button btnErregistratu = view.findViewById(R.id.btnJarraitu);

        String[] motak = {"Bezeroa", "Entrenatzailea"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, motak);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMota.setAdapter(adapter);

        TextView LinkErregistratu = view.findViewById(R.id.LinkErregistratu);
        LinkErregistratu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginFragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, loginFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        editJaiotzeData = view.findViewById(R.id.textDate);
        editJaiotzeData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment newFragment = new DatePickerFragment
                        (RegisterFragment.this);
                newFragment.show(getParentFragmentManager(), "datePicker");
            }
        });

        btnErregistratu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String izena = txtIzena.getText().toString().trim();
                String abizenak = txtAbizenak.getText().toString().trim();
                String erabiltzailea = txtErabiltzaile.getText().toString().toLowerCase().trim();
                String email = textEmail.getText().toString().trim();
                String pasahitza = txtPasahitza.getText().toString().trim();
                String mota = spinnerMota.getSelectedItem().toString();
                // Data bezala jaso eta Firebase-en gordetzeko formatuan pasatzen da.
                Timestamp jaiotzeData =
                        DataFuntzioak.stringToTimestamp(editJaiotzeData.getText().toString());
                dbFuntzioak = new DBFuntzioak(requireContext());  // Pasa el contexto al constructor

                Erabiltzaile erabiltzaile = new Erabiltzaile(izena, abizenak, email, jaiotzeData,
                        mota, pasahitza, erabiltzailea);
                dbFuntzioak.erregistroEgin(erabiltzaile);
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
