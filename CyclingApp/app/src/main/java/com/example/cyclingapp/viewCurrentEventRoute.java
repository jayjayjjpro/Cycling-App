package com.example.cyclingapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdate;
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
import com.google.maps.android.data.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class viewCurrentEventRoute extends Fragment implements OnMapReadyCallback {
    GoogleMap gMap;
    Polyline polyline = null;
    //SeekBar seekWidth;
    List<LatLng> latLngList = new ArrayList<>();
    static List<SubLatLng> subLatLngList = new ArrayList<>();

    LatLng eventLocation;
    float zoomLevel = 13;

    static Marker usermarker=null;

    int first_count = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_view_route, container, false);
        List<SubLatLng>test = (List<SubLatLng>) getArguments().getSerializable("SublatLngLst");
        subLatLngList = test;


        SupportMapFragment supportMapFragment = (SupportMapFragment)
                this.getChildFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);



        return view;
    }


    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;


        //Add kml to map
        Context context = getContext();
        try {
            KmlLayer kmlLayer = new KmlLayer(googleMap, R.raw.cycling_path_network_kml, context);
            kmlLayer.addLayerToMap();
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }

        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                // Get the user's location
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // Create a LatLng object for the user's location
                LatLng userLatLng = new LatLng(latitude, longitude);

                // Add a marker for the user's location
                if(usermarker!= null)usermarker.remove();

                MarkerOptions marker = new MarkerOptions().position(userLatLng).title("Your Location");
                usermarker = googleMap.addMarker(marker);

                // Move the camera to the user's location
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 15f));
            }
        };

// Request for location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);

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
        PolylineOptions polylineOptions = new PolylineOptions().addAll(latLngList).clickable(true);
        polyline = gMap.addPolyline(polylineOptions);


    }





}
