package com.example.cyclingapp;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class EventRepository {
    private static final String EVENTS_COLLECTION = "events";
    private final CollectionReference eventsCollection;

    public EventRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        eventsCollection = db.collection(EVENTS_COLLECTION);
    }

    // Get an event by its ID
    public Task<DocumentSnapshot> getEventById(String eventId) {
        return eventsCollection.document(eventId).get();
    }

    // Get all events
    public Task<QuerySnapshot> getAllEvents() {
        return eventsCollection.get();
    }

    // Add a new event with a unique ID
    public Task<Void> addEvent(Events event) {
        return eventsCollection.add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Update the event ID to match the generated document ID
                        event.setId(documentReference.getId());
                        eventsCollection.document(documentReference.getId()).set(event);
                    }
                })
                .continueWith(new Continuation<DocumentReference, Void>() {
                    @Override
                    public Void then(@NonNull Task<DocumentReference> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }
                        return null;
                    }
                });
    }

    // Update an existing event
    public Task<Void> updateEvent(Events event) {
        return eventsCollection.document(event.getId()).set(event);
    }

    // Delete an event by its ID
    public Task<Void> deleteEventById(String eventId) {
        return eventsCollection.document(eventId).delete();
    }


}
