package com.example.cyclingapp;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ChatCollectionRepository {

    private static final String CHATROOM_COLLECTION = "chatrooms";

    private static final String CHAT_COLLECTION = "chat";
    private final CollectionReference chatCollection;

    public ChatCollectionRepository(ChatRoomModel chatRoomModel) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String chatRoomID = chatRoomModel.getChatroomID();
        chatCollection = db.collection(CHATROOM_COLLECTION).document(chatRoomID).collection(CHAT_COLLECTION);
    }

    // Get all chat messages
    public Task<QuerySnapshot> getAllChatMessages() {
        return chatCollection.orderBy("timestamp", Query.Direction.DESCENDING).get();
    }

    // Get all chat messages query
    public Query getAllChatMessagesAsQuery() {
        return chatCollection.orderBy("timestamp", Query.Direction.DESCENDING);
    }



    public Task<Void> addChatMessage(ChatMessage chatMessage) {

        return chatCollection.add(chatMessage)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Update the event ID to match the generated document ID
                        chatMessage.setID(documentReference.getId());
                        chatCollection.document(documentReference.getId()).set(chatMessage);
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
