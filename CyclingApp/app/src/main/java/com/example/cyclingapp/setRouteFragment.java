package com.example.cyclingapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
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
    Button btDraw,btClear,btConfirm;
    Polyline polyline = null;
    List<LatLng> latLngList = new ArrayList<>();
    List<Marker> markerList = new ArrayList<>();

    static int first_time_count = 1;
    static int draw_complete = 0;
    private OnDataPass dataPasser;

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        dataPasser = (OnDataPass) context;
    }


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_set_route, container, false);
        seekWidth = view.findViewById(R.id.seek_width);
        btDraw = view.findViewById(R.id.bt_draw);
        btClear = view.findViewById(R.id.bt_clear);
        btConfirm = view.findViewById(R.id.bt_confirmRoute);

        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getChildFragmentManager().findFragmentById(R.id.google_map);
        first_time_count = 1;
        draw_complete = 0;
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

                String width = polylineOptions.toString();
                draw_complete = 1;

            }
        });

        btConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (draw_complete==0){
                    Toast.makeText(getActivity(),"Please Draw Line before confirm!",Toast. LENGTH_SHORT).show();
                }
                else{
                    //dataPasser is to pass data to another activity
                    dataPasser.onDataPass(latLngList);
                }

            }
        });

        btClear.setOnClickListener(new View.OnClickListener() {
            //finding LatLng values in array
            @Override
            public void onClick(View v) {
                //Clear All


                if (polyline != null) polyline.remove();
                for (Marker marker: markerList) marker.remove();
                first_time_count = 1;

                latLngList.clear();
                markerList.clear();
                seekWidth.setProgress(3);
                draw_complete = 0;
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
                MarkerOptions markerOptions = new MarkerOptions().position(latLng);
                //create marker
                if (first_time_count==1){
                    //for starting point, use different color marker.
                    //First marker added to markerList is starting point
                    Marker marker = gMap.addMarker(markerOptions.icon(BitmapDescriptorFactory.
                            defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                    markerList.add(marker);
                    first_time_count--;
                }
                else{
                    Marker marker = gMap.addMarker(markerOptions);
                    markerList.add(marker);
                }

                latLngList.add(latLng);


            }
        });

    }



}