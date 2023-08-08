package com.example.cyclingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MessagingActivity extends AppCompatActivity {

    ChatRoomModel chatRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);


        // Retrieve the incoming Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Check if the Intent contains the custom object
            if (intent.hasExtra("chatroom")) {
                chatRoom = (ChatRoomModel) intent.getParcelableExtra("chatroom");
            }
        }



        //Intent is retrieved.
        //Now, send or retrieve from a collection of the chatroom
        //Create a recyclerview to display the collection

    }
}