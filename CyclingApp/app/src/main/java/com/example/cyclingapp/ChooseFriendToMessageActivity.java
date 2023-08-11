package com.example.cyclingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChooseFriendToMessageActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");
    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
    List<User> usersList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_friend_to_message);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ListView listView = (ListView) findViewById(R.id.usersListView);

        usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                usersList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    User user = documentSnapshot.toObject(User.class);
                    usersList.add(user);
                }

                // Update your UI with the usersList
                UsersAdapter adapter = new UsersAdapter(ChooseFriendToMessageActivity.this, usersList);
                listView.setAdapter(adapter);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                User user = usersList.get(position);

                if (user.getId().equals(userID)){
                    Toast.makeText(ChooseFriendToMessageActivity.this, "you cannot message yourself!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(ChooseFriendToMessageActivity.this, usersList.get(position).getDisplayName() + "creating Chatroom!", Toast.LENGTH_SHORT).show();
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("ChosenUserID", user.getId());
                    resultIntent.putExtra("ChosenUserUsername",user.getDisplayName());
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }

            }
        });



    }
}