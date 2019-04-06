package com.example.asyrofiabdusani.earthquakeapp;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Asyrofi Abdusani on 16/02/2018.
 * earthquake.java
 */

public class Earthquake {
    private double mSkala;
    private String mTempat;
    private Long mWaktu;
    private String mUrl;

    public Earthquake(double defSkala, String defTempat, Long defWaktu, String defurl) {
        mSkala=defSkala;
        mTempat=defTempat;
        mWaktu=defWaktu;
        mUrl=defurl;
    }


    public double getmSkala(){
        return mSkala;
    }
    public String getmTempat(){
        return mTempat;
    }
    public Long getmWaktu(){
        return mWaktu;
    }
    public String getmUrl(){
        return mUrl;
    }

}
