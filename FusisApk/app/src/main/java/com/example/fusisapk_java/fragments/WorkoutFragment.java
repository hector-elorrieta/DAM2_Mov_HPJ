package com.example.fusisapk_java.fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fusisapk_java.AldagaiOrokorrak;
import com.example.fusisapk_java.Erabiltzaile;
import com.example.fusisapk_java.R;

import  com.example.fusisapk_java.DBFuntzioak;

import java.util.Objects;


public class WorkoutFragment extends Fragment {

    private Button btnHistorikoa;
    private Button btnWorkout;
    private TextView textWorkErabiltzaile, textWorkMaila;

    AldagaiOrokorrak aldagaiOrokorrak = new AldagaiOrokorrak();
    DBFuntzioak dbFuntzioak = new DBFuntzioak(getContext());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        CardView linkprofila = view.findViewById(R.id.cardViewProfila);
        btnHistorikoa = view.findViewById(R.id.btnHistorikoa);
        btnWorkout = view.findViewById(R.id.btnTrainerWorkout);

        Erabiltzaile logeatuta = aldagaiOrokorrak.erabiltzaileLogueatuta;

        textWorkErabiltzaile = view.findViewById(R.id.textWorkErabiltzaile);
        textWorkMaila = view.findViewById(R.id.textWorkMaila);
        textWorkErabiltzaile.setText(logeatuta.getErabiltzailea());
        textWorkMaila.setText(logeatuta.getMaila());

        if (logeatuta.getMota().equalsIgnoreCase("Bezeroa")) {
            btnWorkout.setVisibility(View.GONE);
        }

        linkprofila.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerfilFragment profilFragment = new PerfilFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, profilFragment);
                transaction.addToBackStack(null);
                transaction.commit();

            }
        });

        btnHistorikoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HistorikoListFragment historikoListFragment = new HistorikoListFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, historikoListFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        dbFuntzioak.datuakBete(success -> {
            if (success) {
                textWorkErabiltzaile.setText(logeatuta.getErabiltzailea());
                textWorkMaila.setText(logeatuta.getMaila());
            } else {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });

        btnWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WorkoutListFragment workoutListFragment = new WorkoutListFragment();
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, workoutListFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        return view;
    }
}
