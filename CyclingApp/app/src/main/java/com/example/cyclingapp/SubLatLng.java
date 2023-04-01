package com.example.cyclingapp;

public class SubLatLng {
    private String latitude;
    private String longtitude;

    public SubLatLng(){

    }

    public SubLatLng(String lat,String lng){
        this.latitude = lat;
        this.longtitude = lng;
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
