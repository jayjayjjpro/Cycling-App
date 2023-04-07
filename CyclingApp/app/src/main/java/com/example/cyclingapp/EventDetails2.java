package com.example.cyclingapp;

import static androidx.fragment.app.FragmentManager.TAG;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class EventDetails2 extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView eventNameTextView;
    private TextView eventLocationTextView;
    private TextView dateTextView;
    private TextView participantsTextView;
    private Button backButton;
    private Button startButton;
    private String eventId;

    enum Status{
        COMPLETED,
        STARTED,
        NOTSTARTED
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details2);

        // Initialize the Firebase Firestore instance
        db = FirebaseFirestore.getInstance();

        // Get a reference to the UI elements
        eventNameTextView = findViewById(R.id.eventName2);
        eventLocationTextView = findViewById(R.id.locInfo2);
        dateTextView = findViewById(R.id.dateInfo2);
        participantsTextView = findViewById(R.id.partInfo2);
        backButton = findViewById(R.id.back);
        startButton = findViewById(R.id.startButton);

        // Retrieve the event ID from the intent extras
        eventId = getIntent().getStringExtra("event_id");
        ;


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
                        viewRouteFragment fragobj = new viewRouteFragment();
                        Log.d("checkBundle", bundle.toString());
                        fragobj.setArguments(bundle);

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_route2
                                , fragobj).commit();

                        // Format the startTime as a string
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        String dateString = dateFormat.format(startTime.toDate());


                        // Set the UI elements with the event details
                        eventNameTextView.setText(eventName);
                        eventLocationTextView.setText(location);
                        dateTextView.setText(dateString);
                        participantsTextView.setText(Integer.toString(participants.size()));
                    }
                });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openHomeActivity();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCurrentEvent();
            }
        });


    }

    private void openHomeActivity() {
        Intent intent = new Intent(EventDetails2.this, HomeActivity.class);
        startActivity(intent);
    }

    private void openCurrentEvent() {

        Intent intent = new Intent(EventDetails2.this, CurrentEvent.class);
        startActivity(intent);

    }
}












