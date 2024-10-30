package com.example.fusisapk_java;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class PerfilFragment extends Fragment {
    private EditText txtIzena, txtAbizenak,
            editJaiotzeData;
    private TextView txtEmail;
    private Button btnAtzera,btnItxiSaioa;
    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseUser erabiltzaileLogeatuta;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);
        txtIzena = view.findViewById(R.id.textIzena);
        txtAbizenak = view.findViewById(R.id.textAbizena);
        txtEmail = view.findViewById(R.id.textEmail);
        editJaiotzeData = view.findViewById(R.id.textDate);
        Button btnAtzera = view.findViewById(R.id.btnAtzera);
        Button btnItxiSaioa = view.findViewById(R.id.btnItxiSaioa);
        Button btnGorde = view.findViewById(R.id.btnGorde);


        erabiltzaileLogeatuta = FirebaseAuth.getInstance().getCurrentUser();
        Log.e("erabiltzaileLogeatuta", erabiltzaileLogeatuta.toString());

        if (erabiltzaileLogeatuta != null) {
            String email = erabiltzaileLogeatuta.getEmail();
            if (email != null) {
                datuakLortuPorEmail(email);
            }else {
                Log.e("Erabiltzailea", "Erabiltzailea ez dago logeatuta");
            }
        }

        btnItxiSaioa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                LoginFragment loginFragment = new LoginFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, loginFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btnAtzera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                HomeFragment homeFragment = new HomeFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
//                transaction.replace(R.id.fragment_container, homeFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        btnGorde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (izenaValidatu() & abizenakValidatu() & jaiotzeDataValidatu()) {
                    String izena = txtIzena.getText().toString().trim();
                    String abizenak = txtAbizenak.getText().toString().trim();
                    Timestamp jaiotzeData = DataFuntzioak.stringToTimestamp(editJaiotzeData.getText().toString());
                    editarDatosPerfil();
                    Log.d("Validación", "Todos los datos son válidos. Guardando...");
                } else {
                    Log.e("Validación", "Hay errores en el formulario.");
                }
            }
        });
        return view;



    }



    private void editarDatosPerfil() {
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
                                .addOnSuccessListener(aVoid ->
                                        Toast.makeText(getContext(), "Datos actualizados correctamente", Toast.LENGTH_SHORT).show())
                                .addOnFailureListener(e ->
                                        Toast.makeText(getContext(), "Error al actualizar datos", Toast.LENGTH_SHORT).show());
                    } else {
                        Toast.makeText(getContext(), "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error al buscar usuario", Toast.LENGTH_SHORT).show());
    }




                    private boolean izenaValidatu(){
                        String izena = txtIzena.getText().toString();
                        if(izena.isEmpty()){
                            txtIzena.setError("Izena sartu behar duzu");
                            return false;
                        }
                        return true;
                    }

                    private boolean abizenakValidatu(){
                        String abizenak = txtAbizenak.getText().toString();
                        if(abizenak.isEmpty()){
                            txtAbizenak.setError("Abizenak sartu behar dituzu");
                            return false;
                        }
                        return true;
                    }

                    private boolean emailaValidatu(){
                        String email = txtEmail.getText().toString();
                        if(email.isEmpty()){
                            txtEmail.setError("Emaila sartu behar duzu");
                            return false;
                        }
                        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                            txtEmail.setError("Emaila okerra");
                            return false;
                        }
                        return true;
                    }

                    private boolean jaiotzeDataValidatu(){
                        String jaiotzeData = editJaiotzeData.getText().toString();
                        if(jaiotzeData.isEmpty()){
                            editJaiotzeData.setError("Jaiotze data sartu behar duzu");
                            return false;
                        }
                        return true;
                    }





    private void datuakLortuPorEmail(String email){
        db.collection("erabiltzaileak")
                .whereEqualTo("mail", email)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot document = queryDocumentSnapshots.getDocuments().get(0);
                        txtIzena.setText(document.getString("izena"));
                        txtAbizenak.setText(document.getString("abizena"));
                        txtEmail.setText(document.getString("mail"));
                        String data = DataFuntzioak.timestampToString(document.getTimestamp("jaiotzedata"));
                        editJaiotzeData.setText(data);
                    } else {
                        Log.d("Firestore", "Ez dago erabiltzaile hori");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Errorea datuak lortzen", e));
    }



}