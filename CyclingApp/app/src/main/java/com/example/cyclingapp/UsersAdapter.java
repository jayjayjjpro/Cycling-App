package com.example.cyclingapp;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class UsersAdapter extends ArrayAdapter<User> {


    public UsersAdapter(Context context, List<User> user) {
        super(context, 0, user);

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
        TextView ID = (TextView) convertView.findViewById(R.id.ID);

        // Populate the data into the template view using the data object
        username.setText(users.getDisplayName());
        ID.setText(users.getId());

        // Return the completed view to render on screen
        return convertView;
    }
}
