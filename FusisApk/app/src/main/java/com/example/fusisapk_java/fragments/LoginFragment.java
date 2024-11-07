package com.example.fusisapk_java.fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fusisapk_java.AldagaiOrokorrak;
import com.example.fusisapk_java.Erabiltzaile;
import com.example.fusisapk_java.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import  com.example.fusisapk_java.DBFuntzioak;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CheckBox checkBoxGogoratu;
    private TextView textEmail, textPasahitza;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();
        checkBoxGogoratu = view.findViewById(R.id.checkBoxGogoratu);
        textEmail = view.findViewById(R.id.textErabiltzailea);
        textPasahitza = view.findViewById(R.id.textPasahitza);

        DBFuntzioak dbFuntzioak = new DBFuntzioak(getContext());

        // Datuak kargatu .dat fitxategitik
        datuakKargatu();

        Button loginButton = view.findViewById(R.id.btnJarraitu);
        loginButton.setOnClickListener(v -> {
            String mail =  textEmail.getText().toString();
            String password = textPasahitza.getText().toString();

            dbFuntzioak.logIn(mail, password, getParentFragmentManager());
            gordeDatuakCheckbox(mail, password);
        });


        TextView linkErregistratu = view.findViewById(R.id.LinkErregistratu);
        linkErregistratu.setOnClickListener(v -> {
            RegisterFragment registerFragment = new RegisterFragment();
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, registerFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });

        return view;
    }

    // Guardar usuario y contraseña en un archivo .dat si el checkbox está marcado
    public void gordeDatuakCheckbox(String mail, String pasahitza) {
        if (checkBoxGogoratu.isChecked()) {
            try (FileOutputStream fos = getActivity().
                    openFileOutput("files\\loginData.dat",
                            Context.MODE_PRIVATE);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(mail);
                oos.writeObject(pasahitza);
            } catch (IOException e) {
                Log.e("LoginFragment", "Error datuak gordetzean", e);
            }
        } else {
            // Si el CheckBox no está marcado, elimina el archivo y limpia los campos
            File file = new File(getActivity().getFilesDir(), "files\\loginData.dat");
            if (file.exists()) {
                if (file.delete()) {
                    Log.d("LoginFragment", "Datuak ezabatuta");
                } else {
                    Log.e("LoginFragment", "Errorea datuak ezabatzean");
                }
            }
            textEmail.setText("");
            textPasahitza.setText("");
        }
    }

    public void datuakKargatu() {
        if (textEmail != null && textPasahitza != null && checkBoxGogoratu != null) {
            try (FileInputStream fis = getActivity().openFileInput("files\\loginData.dat");
                 ObjectInputStream ois = new ObjectInputStream(fis)) {
                String savedEmail = (String) ois.readObject();
                String savedPassword = (String) ois.readObject();
                textEmail.setText(savedEmail);
                textPasahitza.setText(savedPassword);
                checkBoxGogoratu.setChecked(true);
            } catch (IOException | ClassNotFoundException e) {
                Log.d("LoginFragment", "Ez daude datuak", e);
            }
        }
    }
}
