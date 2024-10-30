package com.example.fusisapk_java;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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
        cargarDatosGuardados();

        Button loginButton = view.findViewById(R.id.btnJarraitu);
        loginButton.setOnClickListener(v -> signIn(email.getText().toString(), pasahitza.getText().toString()));

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
    private void guardarDatosSiMarcado(String mail, String pasahitza) {
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
    private void cargarDatosGuardados() {
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

    // Método de inicio de sesión
    private void signIn(String mail, String pasahitza) {
        if (mail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(getActivity(), "Ingrese un correo electrónico válido", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pasahitza.isEmpty()) {
            Toast.makeText(getActivity(), "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(mail, pasahitza)
                .addOnCompleteListener(getActivity(), task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        checkUserInFirestore(user);
                        guardarDatosSiMarcado(mail, pasahitza); // Guardar datos si el checkbox está marcado

                        Toast.makeText(getActivity(), "Autenticación exitosa", Toast.LENGTH_SHORT).show();
                        WorkoutFragment workoutFragment = new WorkoutFragment();
                        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                        transaction.replace(R.id.fragment_container, workoutFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        Log.w("LoginFragment", "Error en la autenticación", task.getException());
                        Toast.makeText(getActivity(), "Error en la autenticación", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Verificación del usuario en Firestore
    private void checkUserInFirestore(FirebaseUser user) {
        db.collection("erabiltzaileak").document(user.getEmail()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("Firestore", "Datos del usuario: " + document.getData());
                        } else {
                            Log.d("Firestore", "Documento no encontrado");
                        }
                    } else {
                        Log.e("Firestore", "Error al obtener documento", task.getException());
                    }
                });
    }
}
