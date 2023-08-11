package com.example.cyclingapp;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.Serializable;
import java.util.List;

public class MessagingActivity extends AppCompatActivity {

    ChatRoomModel chatRoom;
    TextView MessagePartnerUsername;
    EditText Message;
    ImageButton SendButton,BackButton;

    ChatCollectionRepository chatCollectionRepository;

    String userID;
    ChatMessageRecyclerAdapter adapter;
    RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);


        //Create a recyclerview to display the collection https://www.youtube.com/watch?v=er-hKSt1r7E&t=400s


        // Remove the default ActionBar (The one that displays CyclingApp at the top).
        //If still not clear, can remove the code below and take a look.

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        //assigning the textviews, edittexts and buttons
        MessagePartnerUsername = findViewById(R.id.other_username);
        BackButton = findViewById(R.id.back_btn);
        SendButton = findViewById(R.id.message_send_btn);
        Message = findViewById(R.id.chat_message_input);
        recyclerView = findViewById(R.id.chat_recycler_view);


        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();



        // Retrieve the incoming Intent
        Intent intent = getIntent();
        if (intent != null) {
            // Check if the Intent contains the custom object
            if (intent.hasExtra("chatroom")) {
                chatRoom = (ChatRoomModel) intent.getParcelableExtra("chatroom");
                List<String> test = chatRoom.getChat_users_usernames();
                setOtherMessageUserUsernameOnToolbar(chatRoom);
            }
        }




        chatCollectionRepository = new ChatCollectionRepository(chatRoom);





        //Intent is retrieved.
        //TODO Set the messaging partner's username to be displayed.
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //basically executes the function when you press the back button of your phone
                onBackPressed();
            }
        });


        //Now, send or retrieve from a collection of the chatroom
        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Your code to be executed when the button is clicked
                // For example, you can show a Toast message:
                String user_message = String.valueOf(Message.getText());
                Message.getText().clear();

                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(user_message);
                chatMessage.setSenderID(userID);
                chatMessage.setTimestamp(Timestamp.now());

                chatCollectionRepository.addChatMessage(chatMessage).addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("Messages sent","to database");
                            }
                        }
                );
            }
        });

        setupChatRecyclerView();


    }


    void setupChatRecyclerView(){


        Query chatMessagesQuery = chatCollectionRepository.getAllChatMessagesAsQuery();



        FirestoreRecyclerOptions<ChatMessage> options = new FirestoreRecyclerOptions.Builder<ChatMessage>()
                .setQuery(chatMessagesQuery,ChatMessage.class).build();

        adapter = new ChatMessageRecyclerAdapter(options,getApplicationContext());
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setReverseLayout(true);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);
        adapter.startListening();
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.smoothScrollToPosition(0);
            }
        });





    }



    private void setOtherMessageUserUsernameOnToolbar(ChatRoomModel chatroom){
        List<String> users_IDs = null;
        List<String> users_usernames = null;

        users_IDs = chatroom.getUserIDs();
        users_usernames = chatroom.getChat_users_usernames();


        for (int i=0;i<users_IDs.size();i++){
            if (!users_IDs.get(i).equals(userID)){
                MessagePartnerUsername.setText(users_usernames.get(i));
            }
        }

    }
}