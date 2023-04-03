package com.example.cyclingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

public class EventsAdapter extends ArrayAdapter<Events> {
    public EventsAdapter(Context context, List<Events> events) {
        super(context, 0, events);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Events events = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_event, parent, false);
        }
        // Lookup view for data population
        TextView eventName = (TextView) convertView.findViewById(R.id.eventName);
        TextView eventLocation = (TextView) convertView.findViewById(R.id.eventLocation);

        // Populate the data into the template view using the data object
        eventName.setText(events.getName());
        eventLocation.setText(events.getLocation());

        // Return the completed view to render on screen
        return convertView;
    }
}
