package com.example.cyclingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
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
    static List<SubLatLng> subLatLngList = new ArrayList<>();

    LatLng eventLocation;
    float zoomLevel = 13;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_view_route, container, false);
        List<SubLatLng>test = (List<SubLatLng>) getArguments().getSerializable("SublatLngLst");
        subLatLngList = test;

        if(isNetworkAvailable()){
            SupportMapFragment supportMapFragment = (SupportMapFragment)
                    this.getChildFragmentManager().findFragmentById(R.id.google_map);
            supportMapFragment.getMapAsync(this);
        }

        else{
            Toast.makeText(getActivity(), "No internet!", Toast.LENGTH_SHORT).show();
        }






        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        int first_count = 1;
        gMap = googleMap;

        for (int i=0;i<subLatLngList.size();i++){
            SubLatLng temp = subLatLngList.get(i);
            Log.d("lat",temp.getLatitude());
            Double lat = Double.parseDouble(temp.getLatitude());
            Double lng = Double.parseDouble(temp.getLongtitude());
            LatLng value = new LatLng(lat,lng);
            if (first_count++ ==1) eventLocation = value;
            latLngList.add(value);
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

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(eventLocation, zoomLevel);
        gMap.moveCamera(cameraUpdate);



        PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).clickable(true);
        polyline = gMap.addPolyline(polylineOptions);

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }






}