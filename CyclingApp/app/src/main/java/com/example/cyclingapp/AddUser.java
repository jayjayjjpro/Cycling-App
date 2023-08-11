package com.example.cyclingapp;

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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddUser#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddUser extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");

    List<User> usersList = new ArrayList<>();

    List<String> friendList;

    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();



    public AddUser() {
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
        return inflater.inflate(R.layout.fragment_add_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.usersListView);

        usersRef.document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        friendList = (List<String>) documentSnapshot.get("friendList");
                    }
                });




        usersRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                usersList = new ArrayList<>();
                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    User user = documentSnapshot.toObject(User.class);
                    usersList.add(user);
                }

                // Update your UI with the usersList
                UsersAdapter adapter = new UsersAdapter(getActivity(), usersList);
                listView.setAdapter(adapter);
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (usersList.get(position).getId().equals(userID) ){
                    Toast.makeText(getActivity(), "You cannot add yoruself!"
                            , Toast.LENGTH_SHORT).show();
                }

                else{
                    User user = usersList.get(position);
                    if (friendList==null){
                        friendList = new ArrayList<>();
                    }
                    addUsersWithoutDuplicate(user);
                }


            }
        });


        //TODO https://firebase.google.com/docs/database/admin/retrieve-data use this for search function (querying database)

    }

    private void addUsersWithoutDuplicate(User user){
        String temp = user.getId();
        boolean duplicate_friend = false;

        for (int i=0;i<friendList.size();i++){
            if (temp.equals(friendList.get(i))){
                duplicate_friend = true;
                break;
            }
        }

        if (duplicate_friend == false){
            friendList.add(temp);
            Toast.makeText(getActivity(), "user  " + user.getDisplayName()
                    + " is being added as friend!", Toast.LENGTH_SHORT).show();

        }

        else{
            Toast.makeText(getActivity(), "user  " + user.getDisplayName()
                    + " is already a friend!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public void onPause() {
        super.onPause();

        // Get the DocumentReference for the specific user using the UID
        DocumentReference userDocRef = usersRef.document(userID);

// Create a map to store the data you want to update
        Map<String, Object> userData = new HashMap<>();
        userData.put("friendList", friendList);

        // Update the user's document with the new data
        userDocRef.update(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Data successfully written to the document
                        // You can perform any required action on success
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Error writing data to the document
                        Log.e("friend list update error", "Error updating document: " + e.getMessage());
                    }
                });
    }
}