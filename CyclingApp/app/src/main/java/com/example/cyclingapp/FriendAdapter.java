package com.example.cyclingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

public class FriendAdapter extends ArrayAdapter<Friend> {

    public FriendAdapter(Context context, List<Friend> friends) {
        super(context, 0, friends);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Friend friend = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_friend, parent, false);
        }
        // Lookup view for data population
        TextView username = (TextView) convertView.findViewById(R.id.username_friend);
        TextView ID = (TextView) convertView.findViewById(R.id.ID_friend);


        // Populate the data into the template view using the data object
        username.setText(friend.getUsername());
        ID.setText(friend.getID());

        // Return the completed view to render on screen
        return convertView;
    }
}
