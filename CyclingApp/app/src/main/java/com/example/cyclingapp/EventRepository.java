package com.example.cyclingapp;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

public class EventRepository {
    private static final String EVENTS_COLLECTION = "events";
    private static final String COMPLETED_EVENTS_COLLECTION = "completedEvents";
    private static final String USERS_COLLECTION = "users";
    private final CollectionReference eventsCollection;

    private final CollectionReference completedEventsCollection;
    private final CollectionReference usersCollection;

    public EventRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        eventsCollection = db.collection(EVENTS_COLLECTION);
        completedEventsCollection = db.collection(COMPLETED_EVENTS_COLLECTION);
        usersCollection = db.collection(USERS_COLLECTION);
    }

    // Get an event by its ID
    public Task<DocumentSnapshot> getEventById(String eventId) {
        return eventsCollection.document(eventId).get();
    }

    // Get all events
    public Task<QuerySnapshot> getAllEvents(OnSuccessListener<QuerySnapshot> onSuccessListener) {
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

    //Move the completed events to Completed events collection and add it to user collection
    public Task<Void> moveToCompletedEvents(String eventId, Events event, String userId) {
        DocumentReference eventDocument = eventsCollection.document(eventId);
        DocumentReference completedEventDocument = completedEventsCollection.document(eventId);
        DocumentReference userDocument = usersCollection.document(userId);

        return FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                // Delete the event from the events collection
                transaction.delete(eventDocument);

                // Add the event to the completed events collection
                transaction.set(completedEventDocument, event);

                // Update the user's completedEvents list
                transaction.update(userDocument, "completedEvents", FieldValue.arrayUnion(eventId));

                return null;
            }
        });
    }

    enum Status{
        COMPLETED,
        NOTSTARTED,
        STARTED
    }
    //update event status
    public Task<Void> updateStatus(String eventId, Status status) {
        return eventsCollection.document(eventId).update("status", status);
    }

    //Leave Event
    public Task<Void> leaveEvent(String eventId, String userId) {
        DocumentReference eventRef = eventsCollection.document(eventId);
        DocumentReference userRef = usersCollection.document(userId);

        // Create a batch to ensure both the event and user documents are updated atomically
        return FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                // Remove user from the event's participants
                transaction.update(eventRef, "participants", FieldValue.arrayRemove(userId));

                // Remove event from the user's joinedEvents (assuming the user document is structured this way)
                transaction.update(userRef, "joinedEvents", FieldValue.arrayRemove(eventId));

                return null; // Return null as we're not returning any data
            }
        });
    }




}
