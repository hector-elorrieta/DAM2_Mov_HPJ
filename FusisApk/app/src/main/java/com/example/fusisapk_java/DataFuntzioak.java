package com.example.fusisapk_java;

import com.google.firebase.Timestamp;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataFuntzioak {

    public static String dateToString(Date data){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String dataString = sdf.format(data);
        return dataString;
    }

    public static Date stringToDate(String data){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dataDate = null;
        try {
            dataDate = sdf.parse(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataDate;
    }

    public static String timestampToString(Timestamp data){
        Date dataDate = data.toDate();
        String dataString = dateToString(dataDate);
        return dataString;
    }

    public static Timestamp dateToTimestamp(Date data){
        Timestamp dataTimestamp = new Timestamp(data);
        return dataTimestamp;
    }

    public static Timestamp stringToTimestamp(String data){
        Date dataDate = stringToDate(data);
        Timestamp dataTimestamp = dateToTimestamp(dataDate);
        return dataTimestamp;
    }
}
