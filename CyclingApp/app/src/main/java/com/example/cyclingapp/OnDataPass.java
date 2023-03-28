package com.example.cyclingapp;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface OnDataPass {
    void onDataPass(List<LatLng> loc);
}
