package com.example.cyclingapp;

import java.io.Serializable;
import java.util.HashMap;

public class SubLatLng implements Serializable {
    private String latitude;
    private String longtitude;

    public SubLatLng(){

    }

    public SubLatLng(String lat,String lng){
        this.latitude = lat;
        this.longtitude = lng;
    }

    public SubLatLng(HashMap<String,Object> hashMap){
        this.latitude = (String) hashMap.get("latitude");
        this.longtitude = (String) hashMap.get("longtitude");
    }

    public String getLatitude(){
        return latitude;
    }

    public String getLongtitude(){
        return longtitude;
    }

    public void setLatitude(String lat){
        this.latitude = lat;
    }

    public void setLongtitude(String lng){
        this.longtitude = lng;
    }
}
