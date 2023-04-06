package com.example.cyclingapp;

import static android.app.PendingIntent.getActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import java.util.TimeZone;


public class CreateEventActivity extends AppCompatActivity implements OnDataPass {

    private EventRepository eventRepository;
    private UserRepository userRepository;

    private EditText eventNameInput;
    private EditText eventLocationInput;
    private EditText eventStartTimeInput;
    private Button createEventButton;
    private Button backButton;

    private Button eventDateButton;
    private Button eventTimeButton;
    private Calendar eventDateTime;
    private Button setRouteButton;
    private List<LatLng> latLngList_here;
    private List<SubLatLng> subLatLngList = new ArrayList<SubLatLng>();

    private Calendar selectedDate;
    private Calendar selectedTime;

    //these 2 variables are to check if user has set date and time
    private int date_check = 0;
    private int time_check = 0;

    private int name_check = 0;
    private int location_desc_check = 0;

    private int latLnginput_check = 0;



    // private EditText eventDateInput;
    //private EditText eventTimeInput;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        // Initialize the event repository and UI components
        eventRepository = new EventRepository();
        userRepository = new UserRepository();
        eventNameInput = findViewById(R.id.event_name_input);
        eventLocationInput = findViewById(R.id.event_location_input);
       // eventStartTimeInput = findViewById(R.id.event_start_time_input);
        createEventButton = findViewById(R.id.create_event_button);

        eventDateButton = findViewById(R.id.event_date_button);
        eventTimeButton = findViewById(R.id.event_time_button);
        backButton = findViewById(R.id.go_back);
        eventDateTime = Calendar.getInstance();

        selectedDate = Calendar.getInstance();
        selectedTime = Calendar.getInstance();


        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_create_event
                ,new setRouteFragment()).commit();



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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                openHomeActivity();
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
                        date_check=1;
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
                        time_check=1;
                    }
                },
                eventDateTime.get(Calendar.HOUR_OF_DAY),
                eventDateTime.get(Calendar.MINUTE),
                true
        );
        timePickerDialog.show();
    }


    private void createEvent() {
        name_check = 0;
        location_desc_check = 0;

        String eventName = eventNameInput.getText().toString();
        String eventLocation = eventLocationInput.getText().toString();
        Log.d("check eventnameINput",eventName);

        if (eventName.length()>=1)name_check = 1;
        if (eventLocation.length()>=1)location_desc_check = 1;



        if (date_check==0 || time_check==0 || latLnginput_check==0 || location_desc_check ==0 ||name_check==0) {
            Toast.makeText(getApplicationContext(),"Please fill in all fields!",Toast. LENGTH_SHORT).show();
            return;
        }



       // String eventStartTimeString = eventStartTimeInput.getText().toString();
        //Date eventStartTime;
        TimeZone singaporeTimeZone = TimeZone.getTimeZone("Asia/Singapore");
        eventDateTime.setTimeZone(singaporeTimeZone);
        Date eventStartTime = eventDateTime.getTime();


        // Get the actual user ID from Firebase Authentication
        String creatorId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        List<String> participants = new ArrayList<>();

        // Create a new event object with the user input and user ID
        Events event = new Events(null, eventName, eventStartTime, eventLocation, creatorId, participants, Events.Status.NOTSTARTED);
        event.setEventLatLngLst(subLatLngList);
        event.addParticipants(creatorId);


        // Add the event to the repository
        eventRepository.addEvent(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Get the user document
                        userRepository.getUserById(creatorId)
                                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        User user = documentSnapshot.toObject(User.class);
                                        if (user != null) {
                                            // Update the user's createdEvents list and save it back to Firestore
                                            List<String> createdEvents = user.getCreatedEvents();
                                            if (createdEvents == null) {
                                                createdEvents = new ArrayList<>();
                                            }
                                            createdEvents.add(event.getId());
                                            user.setCreatedEvents(createdEvents);
                                            userRepository.updateUser(user);
                                        }
                                    }
                                });

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
    private void openHomeActivity() {
        Intent intent = new Intent(CreateEventActivity.this, HomeActivity.class);
        startActivity(intent);
    }


    @Override
    public void onDataPass(List<com.google.android.gms.maps.model.LatLng> latLngList) {
        latLngList_here = latLngList;
        String lat;
        Double lat_num;
        String lng;
        Double lng_num;
        SubLatLng temp;

        for (int i=0;i<latLngList.size();i++){
            Log.d("receive LatLngList",latLngList_here.get(i).toString());
            lat_num = latLngList.get(i).latitude;
            lat = lat_num.toString();
            Log.d("lat",lat);
            lng_num = latLngList.get(i).longitude;
            lng = lng_num.toString();
            Log.d("lng",lng);
            temp = new SubLatLng(lat,lng);
            subLatLngList.add(temp);
        }


    }
    public void checkDataPass(int check){
        latLnginput_check = check;
    }
}
