package com.example.cyclingapp;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;
    User current_user = null;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        //following code is for NAVBAR
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        drawerToggle = new ActionBarDrawerToggle(this,drawerLayout, R.string.open,R.string.close);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setUsernameAndID();


        //For now, we will use Event fragment as our home page
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new EventFragment()).commit();

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home: {
                        Toast.makeText(HomeActivity.this, "Home Selected", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new EventFragment()).commit();
                        break;
                    }
                    case R.id.event: {
                        Toast.makeText(HomeActivity.this, "Event Selected", Toast.LENGTH_SHORT).show();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new EventFragment()).commit();
                        break;

                    }
                    case R.id.profile: {
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
                        break;
                    }

                    case R.id.upcoming:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UpcomingEventFragment()).commit();
                        break;
                    }
                    case R.id.map:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MapFragment()).commit();
                        break;
                    }
                    case R.id.viewRoute:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new viewRouteFragment()).commit();
                        break;

                    }

                    case R.id.createEvent: {
                        openCreateEventActivity();
                        break;
                    }
                    case R.id.logout:{
                        AuthUI.getInstance().signOut(HomeActivity.this).addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {

                                // below method is used after logout from device.
                                Toast.makeText(HomeActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();

                                // below line is to go to MainActivity via an intent.
                                Intent i = new Intent(HomeActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        });;
                        Toast.makeText(HomeActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();

                        // below line is to go to MainActivity via an intent.
                        Intent i = new Intent(HomeActivity.this, MainActivity.class);
                        startActivity(i);
                    }

                }drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        //end of NAVBAR code









    }

    public void onBackPressed(){
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    private void openCreateEventActivity() {
        Intent intent = new Intent(HomeActivity.this, CreateEventActivity.class);
        startActivity(intent);
    }

    private void setUsernameAndID(){
        String participantId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        UserRepository userRepository = new UserRepository();
        Task<DocumentSnapshot> documentSnapshot = userRepository.getUserById(participantId);

        View headerView = navigationView.getHeaderView(0);
        TextView usernameTextView = headerView.findViewById(R.id.username);
        TextView userIDTextView = headerView.findViewById(R.id.userID);

        documentSnapshot.addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    User user = documentSnapshot.toObject(User.class);
                    // Do something with the User object
                    current_user = user;
                    usernameTextView.setText(current_user.getDisplayName());
                    userIDTextView.setText("userID: " + current_user.getId());
                    Log.d("user",current_user.getDisplayName());
                }
            }
        });
    }
}
