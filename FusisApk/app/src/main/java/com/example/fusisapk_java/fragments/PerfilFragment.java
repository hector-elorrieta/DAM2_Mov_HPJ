package com.example.fusisapk_java.fragments;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fusisapk_java.AldagaiOrokorrak;
import com.example.fusisapk_java.DataFuntzioak;
import com.example.fusisapk_java.Erabiltzaile;
import com.example.fusisapk_java.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class PerfilFragment extends Fragment {
    private EditText txtIzena, txtAbizenak, editJaiotzeData;
    private TextView txtEmail;
    private Button btnAtzera, btnItxiSaioa, btnGorde;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean datuAldatutak = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        Erabiltzaile logueatuta = AldagaiOrokorrak.erabiltzaileLogueatuta;

        txtIzena = view.findViewById(R.id.textIzena);
        txtAbizenak = view.findViewById(R.id.textAbizena);
        txtEmail = view.findViewById(R.id.textEmail);
        editJaiotzeData = view.findViewById(R.id.textDate);
        btnAtzera = view.findViewById(R.id.btnAtzera);
        btnItxiSaioa = view.findViewById(R.id.btnItxiSaioa);
        btnGorde = view.findViewById(R.id.btnGorde);

        Button btnAtzera = view.findViewById(R.id.btnAtzera);
        btnGorde.setEnabled(false);

        txtIzena.setText(logueatuta.getIzena());
        txtAbizenak.setText(logueatuta.getAbizena());
        txtEmail.setText(logueatuta.getMail());
        editJaiotzeData.setText(DataFuntzioak.timestampToString(logueatuta.getJaiotzeData()));

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch darkModeSwitch =
                view.findViewById(R.id.switch_dark_mode);
        darkModeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
        });

        @SuppressLint("UseSwitchCompatOrMaterialCode") Switch languageSwitch = view.findViewById(R.id.Hizkuntza);
        languageSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                setLocale("es");
            } else {
                setLocale("");
            }
        });

        btnAtzera.setOnClickListener(v -> {
            WorkoutFragment workoutFragment = new WorkoutFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, workoutFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        btnItxiSaioa.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            LoginFragment loginFragment = new LoginFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, loginFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        // TextWatcher aldatzen den bakoitzean, datuak aldatu direla jakiteko
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                datuAldatutak = true;
                btnGorde.setEnabled(true);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };

        txtIzena.addTextChangedListener(textWatcher);
        txtAbizenak.addTextChangedListener(textWatcher);
        editJaiotzeData.addTextChangedListener(textWatcher);

        btnGorde.setOnClickListener(v -> {
            if (datuAldatutak && izenaValidatu() & abizenakValidatu() & jaiotzeDataValidatu()) {
                datuakEditatu();
                datuAldatutak = false;
                btnGorde.setEnabled(false);
            }
        });

        return view;
    }

    private void datuakEditatu() {
        String izena = txtIzena.getText().toString();
        String abizenak = txtAbizenak.getText().toString();
        String email = txtEmail.getText().toString();
        String jaiotzeData = editJaiotzeData.getText().toString();

        Map<String, Object> erabiltzaileEguneratua = new HashMap<>();
        erabiltzaileEguneratua.put("izena", izena);
        erabiltzaileEguneratua.put("abizena", abizenak);
        erabiltzaileEguneratua.put("jaiotzedata", DataFuntzioak.stringToTimestamp(jaiotzeData));

        db.collection("erabiltzaileak")
                .whereEqualTo("mail", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        String documentId = queryDocumentSnapshots.getDocuments().get(0).getId();
                        db.collection("erabiltzaileak").document(documentId)
                                .update(erabiltzaileEguneratua)
                                .addOnSuccessListener(aVoid -> Toast.makeText(getContext(),
                                        "Datos actualizados correctamente", Toast.LENGTH_SHORT).show());

                    } else {
                        Toast.makeText(getContext(), "Ez da erabiltzailea aurkitu",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(getContext(), "Error",
                        Toast.LENGTH_SHORT).show());
    }

    private void setLocale(String langCode) {
        // Crear un nuevo Locale con el código de idioma (por ejemplo, "es" o "default")
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);

        // Configurar el nuevo idioma en los recursos
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);

        // Actualizar la configuración global
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        // Recargar el fragmento actual para aplicar los cambios
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, new PerfilFragment());
        transaction.addToBackStack(null);
        transaction.commit();
    }
    private boolean izenaValidatu() {
        String izena = txtIzena.getText().toString();
        if (izena.isEmpty()) {
            txtIzena.setError("Izena sartu behar duzu");
            return false;
        }
        return true;
    }

    private boolean abizenakValidatu() {
        String abizenak = txtAbizenak.getText().toString();
        if (abizenak.isEmpty()) {
            txtAbizenak.setError("Abizenak sartu behar dituzu");
            return false;
        }
        return true;
    }

    private boolean jaiotzeDataValidatu() {
        String jaiotzeData = editJaiotzeData.getText().toString();
        if (jaiotzeData.isEmpty()) {
            editJaiotzeData.setError("Jaiotze data sartu behar duzu");
            return false;
        }
        return true;
    }
}