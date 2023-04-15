package com.example.cyclingapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the  factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private View rootView;
    private Button button;
    private TextView distTextView;
    private Button calories;
    private double totalDistance = 0.0;



    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference eventsRef = db.collection("events");
    List<Events> eventsList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        distTextView = rootView.findViewById(R.id.distProfileInfo);
        calories = rootView.findViewById(R.id.calProfile);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.profileListView);

        if (isNetworkAvailable()){
            String participantId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.d("participantId", participantId);

            eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            eventsList = new ArrayList<>();
                            totalDistance = 0.0;
                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                Events event = documentSnapshot.toObject(Events.class);
                                if (event.checkParticipantDuplicate(participantId) && event.getStatus() == Events.Status.COMPLETED) {
                                    eventsList.add(event);
                                    totalDistance += event.setEstimatedDistanceInKM(event.getEstimatedDistanceInKM()); // add distance to total distance
                                }
                            }
                            // set the total distance to the distTextView
                            distTextView.setText(String.format("%.2f", totalDistance) + " km");

                            // Update your UI with the eventsList
                            EventsAdapter adapter = new EventsAdapter(getActivity(), eventsList);
                            listView.setAdapter(adapter);
                        }
                    });

                    //listView.setOnItemClickListener(this);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                            Toast.makeText(getActivity(), eventsList.get(position).getName(), Toast.LENGTH_SHORT).show();
                            Events event = eventsList.get(position);
                            String eventName = event.getName();
                            String eventID = event.getId();
                            Log.d("Event Details", "Event Clicked: ", new RuntimeException(eventName));
                            Intent eventDetails = new Intent(getContext(), EventDetails3.class);
                            eventDetails.putExtra("event_id", eventID);
                            startActivity(eventDetails);
                        }
                    });

                    calories.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                            builder.setTitle("Enter Your Weight in KG");

                            // Set up the layout of the AlertDialog
                            final EditText input = new EditText(getContext());
                            input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                            builder.setView(input);

                            // Set up the buttons of the AlertDialog
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // Get the weight entered by the user and calculate the average calories burned
                                    String weightString = input.getText().toString();
                                    double weight = Double.parseDouble(weightString);

                                    // Calculate the total calories burned
                                    double avgCaloriesBurnedPerKgPerKm = 0.57;
                                    double totalCaloriesBurned = weight * avgCaloriesBurnedPerKgPerKm * totalDistance;

                                    // Convert the button to a TextView and display the answer
                                    calories.setText("Total calories burned: " + String.format("%.2f", totalCaloriesBurned));
                                    calories.setEnabled(false); // disable the button
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            // Show the AlertDialog
                            builder.show();
                        }

                    });
                }


            });
        }

        else{
            Toast.makeText(getActivity(),"no Internet!",Toast. LENGTH_SHORT).show();
        }



    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
