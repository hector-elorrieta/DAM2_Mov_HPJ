package com.example.fusisapk_java;

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
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class
LoginFragment extends Fragment {
    // FirebaseAuth is a class that provides methods for authentication.
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        mAuth = FirebaseAuth.getInstance();

        Button Login = view.findViewById(R.id.btnJarraitu);
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView erabiltzaile = view.findViewById(R.id.textErabiltzailea);
                TextView pasahitza = view.findViewById(R.id.textPasahitza);
                signIn(erabiltzaile.getText().toString(), pasahitza.getText().toString());
            }
        });
        TextView LinkErregistratu = view.findViewById(R.id.LinkErregistratu);
        LinkErregistratu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterFragment registerFragment = new RegisterFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, registerFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    private void signIn(String erabiltzaile, String password) {
        mAuth.signInWithEmailAndPassword(erabiltzaile, password)
                .addOnCompleteListener(getActivity(), task -> {
                    Log.e("TAG", task.toString());
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        checkUserInFirestore(user);
                        Log.e("TAG", user.getDisplayName());
                    } else {
                        Log.e("TAG", "WAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
                        Log.w("TAG", "signInWithUser:failure", task.getException());
                        Toast.makeText(getActivity(), "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void checkUserInFirestore(FirebaseUser user) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("erabiltzaileak").document(user.getDisplayName()).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        } else {
                            Log.d("TAG", "No such document");
                        }
                    } else {
                        Log.d("TAG", "get failed with ", task.getException());
                    }
                });
    }
}