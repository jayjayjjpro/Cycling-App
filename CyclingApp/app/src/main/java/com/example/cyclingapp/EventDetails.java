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

public class EventDetails extends AppCompatActivity {

    private FirebaseFirestore db;
    private TextView eventNameTextView;
    private TextView eventLocationTextView;
    private TextView dateTextView;
    private TextView participantsTextView;
    private Button joinButton;
    private String eventId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_details);

        // Initialize the Firebase Firestore instance
        db = FirebaseFirestore.getInstance();

        // Get a reference to the UI elements
        eventNameTextView = findViewById(R.id.eventName);
        eventLocationTextView = findViewById(R.id.locInfo);
        dateTextView = findViewById(R.id.dateInfo);
        participantsTextView = findViewById(R.id.partInfo);

        // Retrieve the event ID from the intent extras
        eventId = getIntent().getStringExtra("event_id");;

        if(eventId != null) {

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

            joinButton = findViewById(R.id.joinEvent);

            joinButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    String userID = auth.getCurrentUser().getUid();
                    FirebaseUser currentUser = auth.getCurrentUser();
                    DocumentReference eventRef = db.collection("events").document(eventId);
                    DocumentReference userRef = db.collection("users").document(currentUser.getUid());
                    //DocumentReference userDocRef = userRef.collection("users").document(userID);


                    eventRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            List<String> participants = (List<String>) documentSnapshot.get("participants");

                            if (participants.contains(userID)) {
                                Toast.makeText(EventDetails.this, "You have already joined this event", Toast.LENGTH_SHORT).show();
                            } else {
                                eventRef.update("participants", FieldValue.arrayUnion(userID))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(EventDetails.this, "Joined event successfully", Toast.LENGTH_SHORT).show();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @SuppressLint("RestrictedApi")
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(EventDetails.this, "Failed to join event", Toast.LENGTH_SHORT).show();
                                                Log.e(TAG, "Error joining event", e);
                                            }
                                        });
                                // Update the joinedEvents array for the current user
                                userRef.update("joinedEvents", FieldValue.arrayUnion(eventId))
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Log.d("EventFragment", "User joined event successfully");
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Log.e("EventFragment", "Failed to update user data: " + e.getMessage());
                                            }
                                        });
                            }
                        }
                    });
                }
            });
        }
        else {
            Log.e("Event Details", "Invalid event ID");
            Toast.makeText(EventDetails.this, "Invalid event ID", Toast.LENGTH_SHORT).show();

        }
    }
}

