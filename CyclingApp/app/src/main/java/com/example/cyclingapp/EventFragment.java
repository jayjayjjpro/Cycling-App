package com.example.cyclingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventFragment# newInstance} factory method to
 * create an instance of this fragment.
 */
public class EventFragment extends Fragment {
    EventRepository eventRepository = new EventRepository();
    UserRepository userRepository = new UserRepository();

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");
    CollectionReference eventsRef = db.collection("events");
    List<Events> eventsList = new ArrayList<>();
    enum Status{
        COMPLETED,
        STARTED,
        NOTSTARTED
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        return view;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.eventListView);

        eventsRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                eventsList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Events event = documentSnapshot.toObject(Events.class);
                    if (event.getStatus()== Events.Status.COMPLETED) continue;
                    eventsList.add(event);
                }

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
                Intent eventDetails = new Intent(getContext(), EventDetails.class);
                eventDetails.putExtra("event_id", eventID);
                startActivity(eventDetails);
            }
        });

    }
}
