package com.example.cyclingapp;
import android.app.Notification;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;

public class HomeActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ActionBarDrawerToggle drawerToggle;




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
                    case R.id.setRoute:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new setRouteFragment()).commit();
                        break;

                    }
                    case R.id.viewRoute:{
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new viewRouteFragment()).commit();
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

                }return false;
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
}
