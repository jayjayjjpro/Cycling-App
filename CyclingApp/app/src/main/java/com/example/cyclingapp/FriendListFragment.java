package com.example.cyclingapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FriendListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FriendListFragment extends Fragment {

    FirebaseFirestore db;

    String UserID;
    DocumentReference usersRef;

    List<String> friendList = null;

    UserRepository userRepository;


    public FriendListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();
        UserID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        usersRef = db.collection("users").document(UserID);
        userRepository = new UserRepository();


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView listView = (ListView) view.findViewById(R.id.usersListView);

        Log.d("UserID", UserID);

        usersRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                friendList = (List<String>) documentSnapshot.get("friendList");
                if (friendList != null) {
                    List<String> usernames = null;

                    getUsernamesAndID(friendList, new UsernamesCallback() {
                        @Override
                        public void onUsernamesReceived(List<Friend> friendsList_usernamesAndIDs) {
                            // Do further processing with the list
                            // Create an ArrayAdapter
                            FriendAdapter adapter = new FriendAdapter(getContext(),friendsList_usernamesAndIDs);

                            // Set the ArrayAdapter on the ListView
                            listView.setAdapter(adapter);
                        }
                    });


                }

            }
        });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_friend_list, container, false);
    }

    private void getUsernamesAndID(List<String> friendListIDs, final UsernamesCallback callback) {
        final List<Friend> friends = new ArrayList<>();
        final AtomicInteger count = new AtomicInteger(friendListIDs.size());

        for (String id : friendListIDs) {
            userRepository.getUserById(id).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    User user_friend = documentSnapshot.toObject(User.class);
                    Friend friend = new Friend(user_friend.getDisplayName(), user_friend.getId());
                    friends.add(friend);

                    if (count.decrementAndGet() == 0) {
                        callback.onUsernamesReceived(friends);
                    }
                }
            });
        }
    }

    private interface UsernamesCallback {
        void onUsernamesReceived(List<Friend> friendsList_usernamesAndIDs);
    }
}