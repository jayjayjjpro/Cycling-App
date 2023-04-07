package com.example.cyclingapp;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CurrentEvent extends AppCompatActivity implements LocationListener{

    private TextView distanceTextView;
    private TextView remainingTextView;
    private TextView caloriesTextView;
    private LocationManager locationManager;
    private double totalDistance = 0.0;
    private double remainingDistance = 0.0;
    private double caloriesBurned = 0.0;
    private int weight = 70; // user's weight in kg
    private long startTime;
    private Location previousLocation = null;
    private String eventId;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_event);

        // Retrieve the event ID from the intent extras
        eventId = getIntent().getStringExtra("event_id");;
        Log.d("event ID in currentEvent",eventId);

        // Initialize the Firebase Firestore instance
        db = FirebaseFirestore.getInstance();

        // Query Firestore for the event details using the event ID
        db.collection("events").document(eventId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {

                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        // Get the event details from the document snapshot
                        String eventName = documentSnapshot.getString("name");
                        String location = documentSnapshot.getString("location");
                        SimpleDateFormat dataFormat = new SimpleDateFormat("DD/mm/yyyy");
                        Timestamp startTime = documentSnapshot.getTimestamp("startTime");
                        List<String> participants = (List<String>) documentSnapshot.get("participants");

                        //turning hashmap into sublatlng here
                        List<HashMap<String, String>> rawRoute = (List<HashMap<String, String>>) documentSnapshot.get("eventLatLngLst");
                        ArrayList<SubLatLng> temp = new ArrayList<>();
                        for (HashMap<String, String> entry : rawRoute) {
                            String latitude = entry.get("latitude");
                            String longitude = entry.get("longtitude");
                            Log.d("latitude", latitude);
                            Log.d("longitude", longitude);
                            temp.add(new SubLatLng(latitude, longitude));
                        }

                        //pass data to view route fragment
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("SublatLngLst", temp);
                        viewCurrentEventRoute fragobj = new viewCurrentEventRoute();
                        Log.d("checkBundle", bundle.toString());
                        fragobj.setArguments(bundle);

                        getSupportFragmentManager().beginTransaction().replace(R.id.viewRouteContainer
                                , fragobj).commit();

                        // Format the startTime as a string
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String dateString = dateFormat.format(startTime.toDate());

                    }
                });



        distanceTextView = findViewById(R.id.distanceTextView);
        remainingTextView = findViewById(R.id.remainingDistanceTextView);
        caloriesTextView = findViewById(R.id.caloriesTextView);

        /*

        // Get a reference to the LocationManager
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // Request location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            startTime = System.currentTimeMillis();
        }*/
    }

    @Override
    public void onLocationChanged(Location location) {
        // Calculate distance travelled
        double currentLat = location.getLatitude();
        double currentLng = location.getLongitude();
        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLat);
        currentLocation.setLongitude(currentLng);
        if (totalDistance == 0.0) {
            remainingDistance = 10.0; // Assume a starting distance of 10 km
        } else {
            remainingDistance = remainingDistance - currentLocation.distanceTo(previousLocation) / 1000.0;
        }
        totalDistance += currentLocation.distanceTo(previousLocation) / 1000.0;
        previousLocation = currentLocation;

        // Update the UI with the distance travelled and remaining distance
        distanceTextView.setText(String.format("%.2f km", totalDistance));
        remainingTextView.setText(String.format("%.2f km", remainingDistance));

        // Calculate calories burned
        double speed = location.getSpeed();
        double timeElapsed = (System.currentTimeMillis() - startTime) / 1000.0 / 60.0 / 60.0; // in hours
        double distanceTravelled = currentLocation.distanceTo(previousLocation) / 1000.0; // in km
        double MET = 8.0; // Cycling at moderate pace
        caloriesBurned += (MET * weight * timeElapsed) / 60.0;
        caloriesBurned += (MET * weight * distanceTravelled); // calculate additional calories burned based on distance travelled

        // Update the UI with the calories burned
        caloriesTextView.setText(String.format("%.0f", caloriesBurned));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {}

    @Override
    public void onProviderEnabled(String provider) {}

    @Override
    public void onProviderDisabled(String provider) {}
}