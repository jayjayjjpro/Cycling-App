package com.example.cyclingapp;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Transaction;

public class UserRepository {

    private final FirebaseFirestore db;
    private final String USERS_COLLECTION = "users";
    private final String EVENTS_COLLECTION = "events";
    private final CollectionReference usersCollection;
    private final CollectionReference eventsCollection;

    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.usersCollection = db.collection(USERS_COLLECTION);
        this.eventsCollection = db.collection(EVENTS_COLLECTION);
    }
    //add user
    public Task<Void> addUser(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            return Tasks.forException(new IllegalArgumentException("User ID must not be null or empty."));
        }
        return usersCollection.document(user.getId()).set(user);
    }

    public Task<DocumentSnapshot> getUserById(@NonNull String userId) {
        return usersCollection.document(userId).get();
    }

    //for join events
    //add user to event by updating join events in user collection and userid to events collection participants
    public Task<Void> addUserToEvent(String userId, String eventId) {
        DocumentReference userDocument = usersCollection.document(userId);
        DocumentReference eventDocument = eventsCollection.document(eventId);

        return FirebaseFirestore.getInstance().runTransaction(new Transaction.Function<Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                // Update the user document by adding the eventId to the joinedEvents field
                transaction.update(userDocument, "joinedEvents", FieldValue.arrayUnion(eventId));

                // Update the event document by adding the userId to the participants field
                transaction.update(eventDocument, "participants", FieldValue.arrayUnion(userId));

                return null;
            }
        });
    }


}
