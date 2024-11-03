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

import com.example.fusisapk_java.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import  com.example.fusisapk_java.DBFuntzioak;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class LoginFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CheckBox checkBoxGogoratu;
    private TextView email, pasahitza;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();
        checkBoxGogoratu = view.findViewById(R.id.checkBoxGogoratu);
        email = view.findViewById(R.id.textErabiltzailea);
        pasahitza = view.findViewById(R.id.textPasahitza);

        // Cargar los datos si el archivo existe
        DBFuntzioak dbFuntzioak = new DBFuntzioak(getContext());
        cargarDatosGuardados();

        Button loginButton = view.findViewById(R.id.btnJarraitu);
        loginButton.setOnClickListener(v -> {
            String mail =  String.valueOf(email);  // Reemplaza con el valor real
            String password = String.valueOf(pasahitza);     // Reemplaza con el valor real

            // Llama a logIn pasando el FragmentManager del fragmento
            dbFuntzioak.logIn(mail, password, getParentFragmentManager());
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
    public void guardarDatosSiMarcado(String mail, String pasahitza) {
        if (checkBoxGogoratu.isChecked()) {
            try (FileOutputStream fos = getActivity().openFileOutput("loginData.dat", Context.MODE_PRIVATE);
                 ObjectOutputStream oos = new ObjectOutputStream(fos)) {
                oos.writeObject(mail);
                oos.writeObject(pasahitza);
                Toast.makeText(getContext(), "Datos guardados para inicio automático", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                Log.e("LoginFragment", "Error al guardar datos", e);
            }
        }
    }

    // Cargar datos guardados si el archivo .dat existe y marcar el checkbox
    public void cargarDatosGuardados() {
        try (FileInputStream fis = getActivity().openFileInput("loginData.dat");
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            String savedEmail = (String) ois.readObject();
            String savedPassword = (String) ois.readObject();
            email.setText(savedEmail);
            pasahitza.setText(savedPassword);
            checkBoxGogoratu.setChecked(true);
        } catch (IOException | ClassNotFoundException e) {
            Log.d("LoginFragment", "No se encontraron datos guardados", e);
        }
    }


}
