package com.example.cyclingapp;

import static androidx.fragment.app.FragmentManager.TAG;

import static java.security.AccessController.getContext;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cyclingapp.databinding.FragmentViewRouteBinding;
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
import java.util.Map;

import kotlin.jvm.internal.TypeReference;

public class EventDetails extends AppCompatActivity  {

    private FirebaseFirestore db;
    private TextView eventNameTextView;
    private TextView eventLocationTextView;
    private TextView dateTextView;
    private TextView participantsTextView;
    private Button joinButton;
    private Button backButton;
    private String eventId;

    private Button leaveButton;
    private EventRepository eventRepository;





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
        backButton = findViewById(R.id.back);

        // Retrieve the event ID from the intent extras
        eventId = getIntent().getStringExtra("event_id");

        if (isNetworkAvailable()){
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

                                //turning hashmap into sublatlng here
                                List<HashMap<String, String>> rawRoute = (List<HashMap<String, String>>) documentSnapshot.get("eventLatLngLst");
                                ArrayList<SubLatLng> temp = new ArrayList<>();
                                for (HashMap<String, String> entry : rawRoute) {
                                    String latitude = entry.get("latitude");
                                    String longitude = entry.get("longtitude");
                                    Log.d("latitude",latitude);
                                    Log.d("longitude",longitude);
                                    temp.add(new SubLatLng(latitude, longitude));
                                }

                                //pass data to view route fragment
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("SublatLngLst",temp);
                                viewRouteFragment fragobj = new viewRouteFragment();
                                Log.d("checkBundle",bundle.toString());
                                fragobj.setArguments(bundle);

                                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view_route
                                        ,fragobj).commit();




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
                // Initialize the EventRepository
                eventRepository = new EventRepository();

                // Find the leave button and set up a click listener
                leaveButton = findViewById(R.id.leaveEvent);
                leaveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseAuth auth = FirebaseAuth.getInstance();
                        String userId = auth.getCurrentUser().getUid();

                        // Call the leaveEvent method from the EventRepository
                        eventRepository.leaveEvent(eventId, userId)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(EventDetails.this, "Left event successfully", Toast.LENGTH_SHORT).show();
                                        // You can update the UI here if needed (e.g., decrement participant count)
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EventDetails.this, "Failed to leave event", Toast.LENGTH_SHORT).show();
                                        Log.e(TAG, "Error leaving event", e);
                                    }
                                });
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
        else{
            Toast.makeText(EventDetails.this, "No internet!", Toast.LENGTH_SHORT).show();
        }






        //Go back to main page
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openHomeActivity();
            }
        });



    }

    private void openHomeActivity() {
        Intent intent = new Intent(EventDetails.this, HomeActivity.class);
        startActivity(intent);
    }



    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }





}

