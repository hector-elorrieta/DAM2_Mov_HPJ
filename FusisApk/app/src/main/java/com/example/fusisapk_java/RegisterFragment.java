package com.example.fusisapk_java;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private EditText txtIzena, txtAbizenak, txtErabiltzaile, txtPasahitza, editJaiotzeData, textEmail;
    private Spinner spinnerMota;

    @SuppressLint("MissingInflatedId")
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

        String[] motak = {"Bezeroa", "Prestatzailea"};
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, motak);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMota.setAdapter(adapter);

        editJaiotzeData = view.findViewById(R.id.textDate);
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

        btnErregistratu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarUsuario();
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

    private void registrarUsuario() {
        String izena = txtIzena.getText().toString().trim();
        String abizenak = txtAbizenak.getText().toString().trim();
        String erabiltzailea = txtErabiltzaile.getText().toString().toLowerCase().trim();
        String email = textEmail.getText().toString().trim();
        String pasahitza = txtPasahitza.getText().toString().trim();
        String mota = spinnerMota.getSelectedItem().toString();
        String jaiotzeData = editJaiotzeData.getText().toString();

        if (izena.isEmpty() || abizenak.isEmpty() || erabiltzailea.isEmpty() ||
                pasahitza.isEmpty() || jaiotzeData.isEmpty()) {
            Toast.makeText(getContext(), "Derrigorrezko eremu guztiak bete behar dira",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear usuario en Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, pasahitza)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();

                            Map<String, Object> erabiltzaileBerria = new HashMap<>();
                            erabiltzaileBerria.put("izena", izena);
                            erabiltzaileBerria.put("abizena", abizenak);
                            erabiltzaileBerria.put("mail", email);
                            erabiltzaileBerria.put("erabiltzailea", erabiltzailea);
                            erabiltzaileBerria.put("mota", mota);
                            erabiltzaileBerria.put("pasahitza", pasahitza);
                            //erabiltzaileBerria.put("jaiotzedata", jaiotzeData);


                            db.collection("erabiltzaileak").document(erabiltzaileBerria.get("erabiltzailea").toString())
                                    .set(erabiltzaileBerria)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getContext(),
                                                "Erabiltzailea erregistratu da",
                                                        Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(getContext(),
                                                "Errorea Firestore-n erabiltzailea gordetzean",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(getContext(),
                            "Errorea erabiltzailea erregistratzean",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
