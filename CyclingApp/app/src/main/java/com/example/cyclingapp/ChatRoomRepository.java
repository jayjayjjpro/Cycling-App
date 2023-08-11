package com.example.cyclingapp;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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
    ChatRoomModel temp_chatRoomModel = null;

    public ChatRoomRepository() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        chatroomsCollection = db.collection(CHATROOM_COLLECTION);
    }

    // Get all chatrooms
    public Task<QuerySnapshot> getAllEvents(OnSuccessListener<QuerySnapshot> onSuccessListener) {
        return chatroomsCollection.get();
    }


    public void addChatroom(ChatRoomModel chatRoomModel) {


        chatroomsCollection.document(chatRoomModel.getChatroomID()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                temp_chatRoomModel = documentSnapshot.toObject(ChatRoomModel.class);
                if (temp_chatRoomModel == null) {
                    Log.d("chatRoom did not exist", "null value");

                    // Explicitly set the document ID to match the ChatRoomModel's ID
                    DocumentReference chatroomRef = chatroomsCollection.document(chatRoomModel.getChatroomID());

                    chatroomRef.set(chatRoomModel)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    // Document added successfully
                                    Log.d("chatroom added", "success");
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // Handle failure
                                    Log.d("chatroom not added", "failure");
                                }
                            });

                } else {
                    Log.d("chatroom already exists", "no chatroom is created");
                }
            }
        });



        }


}


