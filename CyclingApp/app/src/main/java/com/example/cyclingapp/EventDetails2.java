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
import java.util.Date;
import java.util.List;

public class EventDetails2 extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView eventNameTextView;
    private TextView eventLocationTextView;
    private TextView dateTextView;
    private TextView participantsTextView;
    private Button backButton;
    private String eventId;


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

        // Retrieve the event ID from the intent extras
        eventId = getIntent().getStringExtra("event_id");;



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


    }

    private void openHomeActivity() {
        Intent intent = new Intent(EventDetails2.this, HomeActivity.class);
        startActivity(intent);
    }
}


