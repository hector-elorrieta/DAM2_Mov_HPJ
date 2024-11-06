package com.example.fusisapk_java;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.fusisapk_java.fragments.WorkoutFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.example.fusisapk_java.fragments.LoginFragment;
import com.google.firebase.firestore.QuerySnapshot;


import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class DBFuntzioak {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private Context context;

    AldagaiOrokorrak aldagaiOrokorrak = new AldagaiOrokorrak();

    // Constructor para inicializar FirebaseAuth, FirebaseFirestore, y el contexto
    public DBFuntzioak(Context context) {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
        this.context = context;
    }

    public void erregistroEgin(Erabiltzaile erabiltzaile) {

        String izena = erabiltzaile.getIzena();
        String abizenak = erabiltzaile.getAbizena();
        String email = erabiltzaile.getMail();
        String erabiltzailea = erabiltzaile.getErabiltzailea();
        String pasahitza = erabiltzaile.getPasahitza();
        String mota = erabiltzaile.getMota();
        Timestamp jaiotzeData = erabiltzaile.getJaiotzeData();

        if (pasahitza.length() < 6 || pasahitza.length() > 16) {
            Toast.makeText(context, "Pasahitza 6 eta 16 karaktere artean egon behar da", Toast.LENGTH_SHORT).show();
            return;
        }
        if (email == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Sartu baliozko posta helbide bat", Toast.LENGTH_SHORT).show();
            return;
        }

        if (izena.isEmpty() || abizenak.isEmpty() || erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
            Toast.makeText(context, "Derrigorrezko eremu guztiak bete behar dira", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, pasahitza)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
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
                            erabiltzaileBerria.put("jaiotzedata", jaiotzeData);
                            erabiltzaileBerria.put("maila", "Hasierakoa");

                            db.collection("erabiltzaileak").document(erabiltzaileBerria.get("erabiltzailea").toString())
                                    .set(erabiltzaileBerria)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context, "Erabiltzailea erregistratu da", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context, "Errorea Firestore-n erabiltzailea gordetzean", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(context, "Ezin da email hori erabili", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void logIn(String mail, String pasahitza, FragmentManager fragmentManager) {

        if (mail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mail).matches()) {
            Toast.makeText(context, "Email formatoa ez dago ondo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pasahitza.isEmpty()) {
            Toast.makeText(context, "Sartu pasahitza", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(mail, pasahitza)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        egiaztatuFirebase(user);

                        Toast.makeText(context, "Ondo logueatu sara", Toast.LENGTH_SHORT).show();
                        WorkoutFragment workoutFragment = new WorkoutFragment();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.fragment_container, workoutFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();
                    } else {
                        Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
                    }
                });

        aldagaiOrokorrak.erabiltzaileLogueatuta = new Erabiltzaile(mail, pasahitza);
        datuakBete();

    }

    private void egiaztatuFirebase(FirebaseUser user) {
        db.collection("erabiltzaileak").document(user.getEmail()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                    } else {
                        Log.e("Firestore", "Error al obtener documento", task.getException());
                    }
                });
    }

    private void datuakBete() {
        db.collection("erabiltzaileak")
                .whereEqualTo("mail", aldagaiOrokorrak.erabiltzaileLogueatuta.getMail())
                .get()
                .addOnCompleteListener(task -> {
                    String userEmail = aldagaiOrokorrak.erabiltzaileLogueatuta.getMail();
                    Log.d("Firestore", "Buscando usuario con email: " + userEmail);

                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();

                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            Log.d("Firestore", "Documento encontrado para el usuario: " + document.getId());

                            // Rellenar datos del usuario logueado con verificación de null
                            aldagaiOrokorrak.erabiltzaileLogueatuta.setIzena(document.getString("izena"));
                            aldagaiOrokorrak.erabiltzaileLogueatuta.setAbizena(document.getString("abizena"));
                            aldagaiOrokorrak.erabiltzaileLogueatuta.setJaiotzeData(document.getTimestamp("jaiotzedata"));
                            aldagaiOrokorrak.erabiltzaileLogueatuta.setMota(document.getString("mota"));
                            aldagaiOrokorrak.erabiltzaileLogueatuta.setErabiltzailea(document.getString("erabiltzailea"));
                            aldagaiOrokorrak.erabiltzaileLogueatuta.setMaila(document.getString("maila"));

                        }
                    }
                });
    }
}

