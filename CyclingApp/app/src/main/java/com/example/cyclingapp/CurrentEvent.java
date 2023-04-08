package com.example.cyclingapp;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CurrentEvent extends AppCompatActivity {

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

    private Button endButton;

    private String eventStatus;
    private Status convertedEventStatus;

    private String currentUserID;

    enum Status{
        COMPLETED,
        STARTED,
        NOTSTARTED
    }

    private EventRepository eventRepository;

    private String creatorID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.current_event);
        eventRepository = new EventRepository();

        // Retrieve the event ID from the intent extras
        eventId = getIntent().getStringExtra("event_id");
        ;
        Log.d("event ID in currentEvent", eventId);

        // Initialize the Firebase Firestore instance
        db = FirebaseFirestore.getInstance();
        endButton = findViewById(R.id.endEvent);

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
                        creatorID = documentSnapshot.getString("creatorId");
                        eventStatus = documentSnapshot.getString("status");
                        convertedEventStatus = Status.valueOf(eventStatus);

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
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the actual user ID from Firebase Authentication
                currentUserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if (convertedEventStatus == Status.STARTED && currentUserID.equals(creatorID) ){
                    Log.d("converted go back to home","Ran this line");
                    eventRepository.updateStatus(eventId, EventRepository.Status.COMPLETED);
                    openHomeActivity();
                }
                else if (convertedEventStatus == Status.STARTED && !currentUserID.equals(creatorID)){
                    Log.d("not creator go back to home","Ran this line");
                    openHomeActivity();
                }
                else {
                    Log.d("go back to home","Ran this line");
                    openHomeActivity();
                }
            }
        });



        distanceTextView = findViewById(R.id.distanceTextView);
        remainingTextView = findViewById(R.id.remainingDistanceTextView);
        caloriesTextView = findViewById(R.id.caloriesTextView);
    }

    private void openHomeActivity() {
        Intent intent = new Intent(CurrentEvent.this, HomeActivity.class);
        startActivity(intent);
    }
}