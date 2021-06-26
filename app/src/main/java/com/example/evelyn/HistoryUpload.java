package com.example.evelyn;

public class HistoryUpload {
    String Date,Hospital;

    public HistoryUpload(String date, String hospital) {
        Date = date;
        Hospital = hospital;
    }

    public HistoryUpload(){

    };

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getHospital() {
        return Hospital;
    }

    public void setHospital(String hospital) {
        Hospital = hospital;
    }
}
