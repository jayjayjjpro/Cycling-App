package com.example.cyclingapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.Timestamp;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ChatRoomAdapter extends ArrayAdapter<ChatRoomModel> {

    public ChatRoomAdapter(Context context, List<ChatRoomModel> chatRooms) {
        super(context, 0, chatRooms);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        ChatRoomModel chatRoomModel = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_chat_room, parent, false);
        }



        // Lookup view for data population
        TextView chatroomID = (TextView) convertView.findViewById(R.id.chatroomID);
        TextView lastMessageTimestamp = (TextView) convertView.findViewById(R.id.lastMessageTimestamp);
        TextView lastMessageSenderID = (TextView) convertView.findViewById(R.id.lastMessageSenderID);
        TextView participants = (TextView) convertView.findViewById(R.id.participants);
        TextView lastMessageSenderUsername = (TextView) convertView.findViewById(R.id.lastMessageSenderUsername);

        // Populate the data into the template view using the data object
        chatroomID.setText("chatroomID: " + chatRoomModel.getChatroomID());
        lastMessageTimestamp.setText("Timestamp: " + convertTime(chatRoomModel.getLastMessageTimestamp()));
        lastMessageSenderID.setText("last Message Sender ID: " + chatRoomModel.getLastMessageSenderID());
        participants.setText("Participants:" + getParticipants(chatRoomModel.getUserIDs()));
        lastMessageSenderUsername.setText("Sender: " + chatRoomModel.getLastMessageSenderUsername());


        // Return the completed view to render on screen
        return convertView;
    }


    private String getParticipants(List<String> userIDs){
        String temp = "";
        for(int i=0;i<userIDs.size();i++){
            temp = temp  + userIDs.get(i) + ", ";
        }
        return temp;
    }

    private String convertTime(Timestamp val){
        // Convert Firebase Timestamp to Date
        Date date = val.toDate();

// Create a SimpleDateFormat object with the desired time format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss 'UTC'Z");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC+8"));

// Format the Date object to the desired format
        String formattedDate = sdf.format(date);

        return formattedDate;

    }

}
