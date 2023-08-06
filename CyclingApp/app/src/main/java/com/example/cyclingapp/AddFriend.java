package com.example.cyclingapp;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddFriend#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddFriend extends Fragment {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference usersRef = db.collection("users");


    List<User> usersList = new ArrayList<>();

    List<String> friendList;

    String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();



    public AddFriend() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_add_friend, container, false);
        // Retrieve the data sent from the activity
        Bundle bundle = getArguments();
        if (bundle != null) {
            friendList = bundle.getStringArrayList("friendList"); // Replace "key" with the same key used in the Activity.
        }


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView listView = (ListView) view.findViewById(R.id.usersListView);

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

                User user = usersList.get(position);

                if (user.getId().equals(userID)){
                    Toast.makeText(getActivity(), "you cannot add yourself!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getActivity(), usersList.get(position).getDisplayName() + "is added as a friend!", Toast.LENGTH_SHORT).show();
                    addFriend(user);
                }

            }
        });





        //TODO https://firebase.google.com/docs/database/admin/retrieve-data use this for search function (querying database)




    }




    @Override
    public void onStop() {
        super.onStop();

        Log.d("onStop called",userID);

        db.collection("users").document(userID)
                .update(
                        "friendList", friendList
                );

        for (int i = 0; i < friendList.size(); i++) {
            Log.d("value in friendList in onStop", friendList.get(i));
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("onStart called", "onstart is called");


        usersRef.document(userID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                friendList = (List<String>) documentSnapshot.get("friendList");
                if (friendList == null) {
                    friendList = new ArrayList<>();
                }
                for (int i = 0; i < friendList.size(); i++) {
                    Log.d("value in friendList in onStart", friendList.get(i));
                }
            }
        });
    }


    //TODO change addFriend to addfriend request!
    // load friend list upon clicking on addFriend fragment
    // then we send add friend request

    private void addFriend(User user){
        boolean duplicate = false;

        //if there are no friends, just add friend to friendlist
        if (friendList.size()==0){
            friendList.add(user.getId());
            Log.d("friend added",user.getId());
        }

        //user has friends, so check for any duplicates
        else{
            String temp_ID = user.getId();
            for (int i=0;i<friendList.size();i++){
                //when the friend added already exists in friendlist, set duplicate flag to true
                if (friendList.get(i).equals(temp_ID)){
                    duplicate = true;
                    break;
                }
            }

            //when the friend already exists, do not add!
            if (duplicate == false){
                friendList.add(temp_ID);
            }

        }
    }
}