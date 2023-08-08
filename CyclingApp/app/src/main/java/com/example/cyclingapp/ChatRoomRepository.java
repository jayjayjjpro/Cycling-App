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

public class ChatRoomRepository {
    private static final String CHATROOM_COLLECTION = "chatrooms";
    private final CollectionReference chatroomsCollection;

    public ChatRoomRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        chatroomsCollection = db.collection(CHATROOM_COLLECTION);
    }

    // Get all chatrooms
    public Task<QuerySnapshot> getAllEvents(OnSuccessListener<QuerySnapshot> onSuccessListener) {
        return chatroomsCollection.get();
    }

    // Add a new chatroom with a unique ID
    public Task<Void> addChatroom(ChatRoomModel chatRoomModel) {
        return chatroomsCollection.add(chatRoomModel)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Update the event ID to match the generated document ID
                        chatroomsCollection.document(documentReference.getId()).set(chatRoomModel);
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


}
