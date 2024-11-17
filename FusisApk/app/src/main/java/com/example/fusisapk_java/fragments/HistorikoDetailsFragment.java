package com.example.fusisapk_java.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.fusisapk_java.AldagaiOrokorrak;
import com.example.fusisapk_java.DataFuntzioak;
import com.example.fusisapk_java.Erabiltzaile;
import com.example.fusisapk_java.Historikoa;
import com.example.fusisapk_java.R;


public class HistorikoDetailsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_historiko_details, container, false);
        Erabiltzaile logueatuta = AldagaiOrokorrak.erabiltzaileLogueatuta;

        String izena = "";
        String denbora = "";
        String maila = "";
        String bideoa = "";
        TextView textIzena = view.findViewById(R.id.textIzena);
        TextView textDenbora = view.findViewById(R.id.textDenbora);
        TextView textMaila = view.findViewById(R.id.textMaila);
        TextView textEgindaDenbora = view.findViewById(R.id.textEgindakoDenbora);
        TextView textEhunekoa = view.findViewById(R.id.textEhunekoa);
        TextView textData = view.findViewById(R.id.textData);
        TextView bideoLink = view.findViewById(R.id.bideoLink);

        Button btnAtzera = view.findViewById(R.id.btnAtzera);

        // Bundle datuak jaso eta erakutsi
        Bundle args = getArguments();
        if (args != null) {
            izena = args.getString("izena");
            denbora = args.getString("denbora");
            maila = args.getString("maila");
            bideoa = args.getString("bideoa");
        }

        for (Historikoa historikoa : logueatuta.getHistorikoak()) {
            if (historikoa.getWorkoutIzena().equals(izena)) {
                textIzena.setText(izena);
                textDenbora.setText("Esperatako denbora: " + denbora + " segundu");
                textMaila.setText("Maila: " + maila);
                textData.setText("Data: " + DataFuntzioak.timestampToString(historikoa.getData()));
                textEgindaDenbora.setText("Egindako denbora: " + historikoa.getDenbora_eginda() + " segundu");
                textEhunekoa.setText("Ehunekoa: " + historikoa.getEhunekoa() + "%");
            }
        }


        String bideoID = bideoa.substring(bideoa.lastIndexOf("/") + 1, bideoa.indexOf("?"));
        String bideoURL = "https://www.youtube.com/watch?v=" + bideoID;

        bideoLink.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(bideoURL));
            intent.putExtra(Intent.EXTRA_REFERRER, Uri.parse("android-app://com.google.android.youtube/"));
            startActivity(intent);
        });

        btnAtzera.setOnClickListener(v -> {
            HistorikoListFragment historikoListFragment = new HistorikoListFragment();
            getParentFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, historikoListFragment).commit();
        });



        return view;
    }
}