package com.example.cyclingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CreateEventActivity extends AppCompatActivity {

    private EventRepository eventRepository;
    private EditText eventNameInput;
    private EditText eventLocationInput;
    private EditText eventStartTimeInput;
    private Button createEventButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Initialize the event repository and UI components
        eventRepository = new EventRepository();
        eventNameInput = findViewById(R.id.event_name_input);
        eventLocationInput = findViewById(R.id.event_location_input);
        eventStartTimeInput = findViewById(R.id.event_start_time_input);
        createEventButton = findViewById(R.id.create_event_button);

        // Set a click listener for the create event button
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });
    }

    private void createEvent() {
        // Get user input for event name, location, and start time
        String eventName = eventNameInput.getText().toString();
        String eventLocation = eventLocationInput.getText().toString();
        String eventStartTimeString = eventStartTimeInput.getText().toString();
        Date eventStartTime;

        // Convert the start time string to a Date object
        try {
            eventStartTime = new Date(Long.parseLong(eventStartTimeString));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid start time format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get the actual user ID from Firebase Authentication
        String creatorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<String> participants = new ArrayList<>();

        // Create a new event object with the user input and user ID
        Events event = new Events(null, eventName, eventStartTime, eventLocation, creatorId, participants);

        // Add the event to the repository
        eventRepository.addEvent(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Show a success message and close the activity
                        Toast.makeText(CreateEventActivity.this, "Event created successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Show an error message if the event creation failed
                        Toast.makeText(CreateEventActivity.this, "Failed to create event", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
