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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;


public class PerfilFragment extends Fragment {
    private TextView txtIzena, txtAbizenak,
            textEmail;
    private EditText editJaiotzeData;
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
        textEmail = view.findViewById(R.id.textEmail);
        editJaiotzeData = view.findViewById(R.id.textDate);
        Button btnAtzera = view.findViewById(R.id.btnAtzera);
        Button btnItxiSaioa = view.findViewById(R.id.btnItxiSaioa);


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
        return view;



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
        String email = textEmail.getText().toString();
        if(email.isEmpty()){
            textEmail.setError("Emaila sartu behar duzu");
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            textEmail.setError("Emaila okerra");
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
                        textEmail.setText(document.getString("mail"));
                        String data = DataFuntzioak.timestampToString(document.getTimestamp("jaiotzedata"));
                        editJaiotzeData.setText(data);
                    } else {
                        Log.d("Firestore", "Ez dago erabiltzaile hori");
                    }
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Errorea datuak lortzen", e));
    }
}