package com.example.cyclingapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatroomsFragment extends Fragment {

    //TODO https://www.youtube.com/watch?v=er-hKSt1r7E
    //TODO learn from the vid how they managed to set the firebase document ID the same as his custom ID
    //1) Create a Message Model
    //   1.1) a unique ID can be generated by comparing ID of both users, then take the smaller id
    //   and concatenate with the longer one.
    //2) store the chatlog in the message model (as a collection)
    //  2.1) FirebaseFirestore.getInstance().collection("chatroom").document(chatroomID);
    //3) Display the chatlog

    private static final int CREATE_NEW_CHAT = 1;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FloatingActionButton floatingActionButton;

    String MessagePartnerID;

    String userID;

    ChatRoomRepository chatRoomRepository;
    UserRepository userRepository;


    String currentUserUsername;


    public ChatroomsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_chatrooms, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        userRepository = new UserRepository();
        chatRoomRepository = new ChatRoomRepository();

        userRepository.getUserById(userID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                currentUserUsername = user.getDisplayName();
            }
        });


        ListView listView = (ListView) view.findViewById(R.id.chatroomListView);

        //retrieving all the chatrooms from the database
        db.collection("chatrooms").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                ArrayList<ChatRoomModel> chatroomList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    boolean part_of_chat = false;
                    ChatRoomModel chatroom = documentSnapshot.toObject(ChatRoomModel.class);
                    part_of_chat = checkIfUserIsPartOfChat(chatroom);
                    //only add the chatroom to the UI if the user is part of the conversation
                    if (part_of_chat==true) {
                        Log.d("chatroom Added","user is part of chat");
                        chatroomList.add(chatroom);
                    }
                }
                Log.d("executed chatroom_db get","executed");

                // Update your UI with the chatRoomList
                ChatRoomAdapter adapter = new ChatRoomAdapter(getActivity(), chatroomList);
                listView.setAdapter(adapter);

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatRoomModel chatRoomModel = (ChatRoomModel) listView.getItemAtPosition(position);
                //Start a messaging activity with intent of the chatroom!
                Intent intent = new Intent(getContext(),MessagingActivity.class);
                intent.putExtra("chatroom",chatRoomModel);
                startActivity(intent);
            }
        });



        floatingActionButton = getView().findViewById(R.id.floatingActionButton);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Your code to handle the FAB click event
                // For example, you can start a new activity or show a dialog

                Intent intent = new Intent(getActivity(), ChooseFriendToMessageActivity.class);
                startActivityForResult(intent, CREATE_NEW_CHAT);

                 //Do note that the activity result will NOT BE RECEIVED HERE.
                //result is received after the onclick

            }
        });









    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CREATE_NEW_CHAT) {
            if (resultCode == Activity.RESULT_OK) {
                // Handle the result data here
                String temp = data.getStringExtra("ChosenUserID");
                MessagePartnerID = temp;

                //After we have the MessagePartnerID, we want to create a chatroom and store in Database
                String chatroomID = generateChatroomID(userID,MessagePartnerID);
                List<String> chatRoom_UserIDs= new ArrayList<>();
                chatRoom_UserIDs.add(userID);
                chatRoom_UserIDs.add(MessagePartnerID);
                Timestamp timestamp = Timestamp.now();


                ChatRoomModel chatRoom = new ChatRoomModel(chatroomID,chatRoom_UserIDs,timestamp,userID,currentUserUsername);
                chatRoomRepository.addChatroom(chatRoom);

            }
        }
    }

    private String generateChatroomID(String current_userID, String other_userID) {

        int result = current_userID.compareTo(other_userID);
        if (result < 0) {
            return current_userID + other_userID;
        } else if (result > 0) {
            return other_userID + current_userID;
        } else return null;

    }

    private boolean checkIfUserIsPartOfChat(ChatRoomModel chatRoomModel){
        List<String> UserIDs;

        UserIDs = chatRoomModel.getUserIDs();

        for (int i=0;i<UserIDs.size();i++){
            if (userID.equals(UserIDs.get(i))){
                return true;
            }
        }

        return false;
    }


}