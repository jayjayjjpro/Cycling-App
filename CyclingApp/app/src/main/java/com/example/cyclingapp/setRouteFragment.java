package com.example.cyclingapp;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class setRouteFragment extends Fragment /**implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener **/{
    GoogleMap gMap;
    SeekBar seekWidth;
    Button btDraw,btClear;
    Polyline polyline = null;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_set_route, container, false);

        View view = inflater.inflate(R.layout.fragment_set_route, container, false);
        seekWidth = view.findViewById(R.id.seek_width);
        btDraw = view.findViewById(R.id.bt_draw);
        btClear = view.findViewById(R.id.bt_clear);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this::onMapReady);

        btDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Draw polyline on Map
                if (polyline!=null)polyline.remove();
                //Create polylineOptions
                PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).clickable(true);
                polyline = gMap.addPolyline(polylineOptions);
                setWidth();
            }
        });

        btClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear All
                if (polyline != null) polyline.remove();
                for (Marker marker: markerList) marker.remove();
                latLngList.clear();
                markerList.clear();
                seekWidth.setProgress(3);
            }
        });


        return view;
    }


    private void setWidth() {
        seekWidth.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //Get Seekbar Progress
                int width = seekWidth.getProgress();
                if (polyline!=null)
                    //set Polyline width
                    polyline.setWidth(width);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }



    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                //create markeroptions

                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                //create marker
                Marker marker = gMap.addMarker(markerOptions);
                //add Latlng and marker
                latLngList.add(latLng);
                markerList.add(marker);
            }
        });

    }



}