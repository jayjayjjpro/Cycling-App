package com.example.cyclingapp;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserRepository {

    private final FirebaseFirestore db;

    public UserRepository() {
        this.db = FirebaseFirestore.getInstance();
    }

    public Task<Void> addUser(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            return Tasks.forException(new IllegalArgumentException("User ID must not be null or empty."));
        }
        return db.collection("users").document(user.getId()).set(user);
    }

    public Task<DocumentSnapshot> getUserById(@NonNull String userId) {
        return db.collection("users").document(userId).get();
    }
}
