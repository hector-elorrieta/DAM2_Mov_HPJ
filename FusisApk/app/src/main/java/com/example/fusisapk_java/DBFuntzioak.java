package com.example.fusisapk_java;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.Patterns;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            Toast.makeText(context, "Pasahitza 6 eta 16 karaktere artean egon behar da",
                                                                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (email == null || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(context, "Sartu baliozko posta helbide bat",
                                                                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (izena.isEmpty() || abizenak.isEmpty() || erabiltzailea.isEmpty() || pasahitza.isEmpty()) {
            Toast.makeText(context, "Derrigorrezko eremu guztiak bete behar dira",
                                                                    Toast.LENGTH_SHORT).show();
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

                            db.collection("erabiltzaileak").document
                                        (erabiltzaileBerria.get("erabiltzailea").toString())
                                    .set(erabiltzaileBerria)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(context,
                                            "Erabiltzailea erregistratu da",
                                                Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(context,
                                            "Errorea Firestore-n erabiltzailea gordetzean",
                                                    Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(context, "Ezin da email hori erabili",
                                                                        Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    public void logIn(String mail, String pasahitza, FragmentManager fragmentManager) {
        if (mail.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(mail).matches() ||
                !mail.contains("@") || !mail.contains(".") || !mail.equals(mail.toLowerCase())) {
            Toast.makeText(context, "Email formatoa ez dago ondo", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pasahitza.isEmpty() || pasahitza.length() < 6) {
            Toast.makeText(context, "Pasahitza okerra", Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.signInWithEmailAndPassword(mail, pasahitza)
                .addOnCompleteListener((Activity) context, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        egiaztatuFirebase(user);

                        Toast.makeText(context, "Ondo logueatu sara", Toast.LENGTH_SHORT).show();

                        // Llamamos a datuakBete con un callback que se ejecutará cuando los
                        // datos se hayan cargado
                        datuakBete(success -> {
                            if (success) {
                                // Los datos están listos, ahora podemos cargar el fragmento
                                WorkoutFragment workoutFragment = new WorkoutFragment();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.replace(R.id.fragment_container, workoutFragment);
                                transaction.addToBackStack(null);
                                transaction.commit();
                            } else {
                                Toast.makeText(context, "Error al cargar los datos",
                                                                        Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        Toast.makeText(context, "Datuak okerrak", Toast.LENGTH_SHORT).show();
                    }
                });

        // Guarda el usuario logueado temporalmente
        aldagaiOrokorrak.erabiltzaileLogueatuta = new Erabiltzaile(mail, pasahitza);
    }


    private void egiaztatuFirebase(FirebaseUser user) {
        db.collection("erabiltzaileak").document(user.getEmail()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                    } else {
                        Log.e("Firestore", "Error al obtener documento",
                                                                            task.getException());
                    }
                });
    }

    public void datuakBete(OnDataLoadCallback callback) {
        db.collection("erabiltzaileak")
                .whereEqualTo("mail", aldagaiOrokorrak.erabiltzaileLogueatuta.getMail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null && !task.getResult().isEmpty()) {
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);
                        aldagaiOrokorrak.erabiltzaileLogueatuta.setIzena
                                                        (document.getString("izena"));
                        aldagaiOrokorrak.erabiltzaileLogueatuta.setAbizena
                                                        (document.getString("abizena"));
                        aldagaiOrokorrak.erabiltzaileLogueatuta.setJaiotzeData
                                                        (document.getTimestamp("jaiotzedata"));
                        aldagaiOrokorrak.erabiltzaileLogueatuta.setMota
                                                        (document.getString("mota"));
                        aldagaiOrokorrak.erabiltzaileLogueatuta.setErabiltzailea
                                                        (document.getString("erabiltzailea"));
                        aldagaiOrokorrak.erabiltzaileLogueatuta.setMaila
                                                        (document.getString("maila"));

                        aldagaiOrokorrak.erabiltzaileLogueatuta.setHistorikoak
                                (hartuHistorikoaErabiltzailea());

                        callback.onDataLoaded(true);
                    } else {
                        callback.onDataLoaded(false);
                    }
                });
    }

    public ArrayList<Historikoa> hartuHistorikoaErabiltzailea() {
        // Crear la lista de resultados
        ArrayList<Historikoa> historikoList = new ArrayList<>();

        // Consulta a la colección "erabiltzaileak"
        db.collection("erabiltzaileak")
                .whereEqualTo("mail", aldagaiOrokorrak.erabiltzaileLogueatuta.getMail())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            QueryDocumentSnapshot document = (QueryDocumentSnapshot)
                                    task.getResult().getDocuments().get(0);

                            CollectionReference historikoaRef = document.getReference().
                                    collection("historikoa");

                            historikoaRef.get()
                                    .addOnCompleteListener(historikoTask -> {
                                        if (historikoTask.isSuccessful() &&
                                                historikoTask.getResult() != null) {
                                            for (QueryDocumentSnapshot historikoDoc :
                                                    historikoTask.getResult()) {
                                                String workoutName =
                                                        historikoDoc.getString("workouts");
                                                Timestamp data =
                                                        historikoDoc.getTimestamp("data");
                                                int egindakoDenbora =
                                                        historikoDoc.getLong
                                                                ("egindako_denbora").intValue();
                                                int ehuneko =
                                                        historikoDoc.getLong
                                                                ("ehuneko").intValue();

                                                Historikoa historikoa =
                                                        new Historikoa(workoutName, data, egindakoDenbora, ehuneko);
                                                historikoList.add(historikoa);
                                            }

                                        }
                                    });
                        } else {
                            Log.e("Error", "No se encontró el usuario");
                        }
                    } else {
                        // Manejo de errores de la consulta principal
                        Log.e("Error","Error al obtener el usuario: " + task.getException().getMessage());
                    }
                });

        return historikoList;
    }

    public void egindakoWorkoutHistorikoan(OnWorkoutListLoadedCallback callback) {
        ArrayList<Workout> workoutsEgindak = new ArrayList<>();
        Erabiltzaile erabiltzaile = aldagaiOrokorrak.erabiltzaileLogueatuta;

        ArrayList<Historikoa> historikoList = erabiltzaile.getHistorikoak();

        getWorkoutList(workoutList -> {
            if (workoutList != null) {
                for (Historikoa historikoa : historikoList) {
                    for (Workout workout : workoutList) {
                        if (historikoa.getWorkoutIzena().equals(workout.getIzena())) {
                            workoutsEgindak.add(workout);
                        }
                    }
                }

                Log.e("Workouts", workoutsEgindak.toString());

                if (callback != null) {
                    callback.onWorkoutListLoaded(workoutsEgindak);
                }
            } else {
                Log.e("Error", "Error al cargar la lista de workouts");
                if (callback != null) {
                    callback.onWorkoutListLoaded(new ArrayList<>());
                }
            }
        });
    }

    public void getWorkoutList(OnWorkoutListDataLoadCallback callback) {
        ArrayList<Workout> workoutList = new ArrayList<>();

        db.collection("workouts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            String izena = document.getString("izena");
                            int denbora = document.getLong("denbora").intValue();
                            String bideoa = document.getString("link");
                            String maila = document.getString("maila");
                            workoutList.add(new Workout(izena, denbora, bideoa, maila));
                        }

                        if (callback != null) {
                            callback.onWorkoutListDataLoaded(workoutList);
                        }
                    } else {
                        if (callback != null) {
                            callback.onWorkoutListDataLoaded(null);
                        }
                    }
                });
    }

    public interface OnWorkoutListDataLoadCallback {
        void onWorkoutListDataLoaded(ArrayList<Workout> workoutList);
    }

    public interface OnWorkoutListLoadedCallback {
        void onWorkoutListLoaded(ArrayList<Workout> workouts);
    }

    // Callback egiaztatzen du Erabiltzailea ondo logueatu dela eta datuak kargatu direla
    public interface OnDataLoadCallback {
        void onDataLoaded(boolean success);
    }


}

