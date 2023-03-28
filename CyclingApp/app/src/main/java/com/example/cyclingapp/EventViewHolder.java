package com.example.cyclingapp;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EventViewHolder extends RecyclerView.ViewHolder {

    public TextView eventName;
    public TextView eventLocation;
    public TextView eventStartTime;

    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        eventName = itemView.findViewById(R.id.event_name);
        eventLocation = itemView.findViewById(R.id.event_location);
        eventStartTime = itemView.findViewById(R.id.event_start_time);
    }
}
