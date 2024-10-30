package com.example.fusisapk_java;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class WorkoutFragment extends Fragment {

    private Button btnWorkout;
    private Button btnHistoriala;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_workout, container, false);

        CardView linkprofila = view.findViewById(R.id.cardViewProfila);
        btnWorkout = view.findViewById(R.id.btnWorkout);
        btnHistoriala = view.findViewById(R.id.btnHistoriala);

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






        return view;
    }
}
