package com.example.cyclingapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;
import java.util.Calendar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.TimeZone;


public class CreateEventActivity extends AppCompatActivity {

    private EventRepository eventRepository;
    private EditText eventNameInput;
    private EditText eventLocationInput;
    private EditText eventStartTimeInput;
    private Button createEventButton;

    private Button eventDateButton;
    private Button eventTimeButton;
    private Calendar eventDateTime;

    private Calendar selectedDate;
    private Calendar selectedTime;

    // private EditText eventDateInput;
    //private EditText eventTimeInput;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Initialize the event repository and UI components
        eventRepository = new EventRepository();
        eventNameInput = findViewById(R.id.event_name_input);
        eventLocationInput = findViewById(R.id.event_location_input);
       // eventStartTimeInput = findViewById(R.id.event_start_time_input);
        createEventButton = findViewById(R.id.create_event_button);

        eventDateButton = findViewById(R.id.event_date_button);
        eventTimeButton = findViewById(R.id.event_time_button);
        eventDateTime = Calendar.getInstance();

        selectedDate = Calendar.getInstance();
        selectedTime = Calendar.getInstance();


        //Set a click listener for time and date
        eventDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        eventTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimePickerDialog();
            }
        });


        // Set a click listener for the create event button
        createEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });
    }
    //for date and time
    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        eventDateTime.set(Calendar.YEAR, year);
                        eventDateTime.set(Calendar.MONTH, month);
                        eventDateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        eventDateButton.setText(String.format("%04d-%02d-%02d", year, month + 1, dayOfMonth));
                    }
                },
                eventDateTime.get(Calendar.YEAR),
                eventDateTime.get(Calendar.MONTH),
                eventDateTime.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        eventDateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        eventDateTime.set(Calendar.MINUTE, minute);
                        eventTimeButton.setText(String.format("%02d:%02d", hourOfDay, minute));
                    }
                },
                eventDateTime.get(Calendar.HOUR_OF_DAY),
                eventDateTime.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }


    private void createEvent() {
        // Get user input for event name, location, and start time
        String eventName = eventNameInput.getText().toString();
        String eventLocation = eventLocationInput.getText().toString();
       // String eventStartTimeString = eventStartTimeInput.getText().toString();
        //Date eventStartTime;
        TimeZone singaporeTimeZone = TimeZone.getTimeZone("Asia/Singapore");
        eventDateTime.setTimeZone(singaporeTimeZone);
        Date eventStartTime = eventDateTime.getTime();

        // Convert the start time string to a Date object
       /* try {
            eventStartTime = new Date(Long.parseLong(eventStartTimeString));
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Invalid start time format", Toast.LENGTH_SHORT).show();
            return;
        }*/

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
