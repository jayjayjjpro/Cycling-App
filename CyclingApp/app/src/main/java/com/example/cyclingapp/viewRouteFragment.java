package com.example.cyclingapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class viewRouteFragment extends Fragment implements OnMapReadyCallback {
    GoogleMap gMap;
    Polyline polyline = null;
    //SeekBar seekWidth;
    List<LatLng> latLngList = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_route, container, false);
        // Inflate the layout for this fragment

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                this.getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);

        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        //This is to test the for loop. For loop will be used for data from database
        //TODO get latLngList from event database and delete the below dummy values for debugging
        LatLng latLng = new LatLng(19.48397357816699,10.392014980316162);
        LatLng latLng2 = new LatLng(22,20);
        latLngList.add(latLng);
        latLngList.add(latLng2);




        for (int i=0;i<latLngList.size();i++){
            LatLng value = latLngList.get(i);
            MarkerOptions markerOptions = new MarkerOptions().position(value);
            if (i==0){
                //Orange color for starting point
                gMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.
                        defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
            }
            else{
                gMap.addMarker(markerOptions);
            }
        }



        PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).clickable(true);
        polyline = gMap.addPolyline(polylineOptions);
    }


}