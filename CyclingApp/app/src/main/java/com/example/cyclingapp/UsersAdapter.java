package com.example.cyclingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<User> {


    public UsersAdapter(Context context, List<User> users) {
        super(context, 0, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        User users = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_user, parent, false);
        }
        // Lookup view for data population
        TextView username = (TextView) convertView.findViewById(R.id.username);

        // Populate the data into the template view using the data object
        username.setText(users.getDisplayName());

        // Return the completed view to render on screen
        return convertView;
    }




}