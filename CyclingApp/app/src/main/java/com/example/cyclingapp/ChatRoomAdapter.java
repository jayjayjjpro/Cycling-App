package com.example.cyclingapp;

import android.content.Context;
import android.util.Log;
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
        TextView participants_ID = (TextView) convertView.findViewById(R.id.participants_ID);
        TextView participants_Username = (TextView) convertView.findViewById(R.id.participants_Username);


        // Populate the data into the template view using the data object
        participants_ID.setText("IDs:" + getParticipantsID(chatRoomModel.getUserIDs()));
        participants_Username.setText("ChatRoom for: " + getParticipantsUsername(chatRoomModel.getChat_users_usernames()));


        // Return the completed view to render on screen
        return convertView;
    }


    private String getParticipantsID(List<String> userIDs){
        String temp = "";
        for(int i=0;i<userIDs.size();i++){
            if (i==userIDs.size()-1){
                temp = temp  + userIDs.get(i);
            }
            else{
                temp = temp  + userIDs.get(i) + ", ";
            }

        }
        return temp;
    }

    private String getParticipantsUsername(List<String> usernames){
        String temp = "";
        for(int i=0;i<usernames.size();i++){
            if(i==usernames.size()-1){
                temp = temp  + usernames.get(i);
            }
            else{
                temp = temp  + usernames.get(i) + ", ";
            }

        }
        return temp;
    }

    private String convertTime(Timestamp val){
        // Convert Firebase Timestamp to Date
        Date date = val.toDate();

// Create a SimpleDateFormat object with the desired time format
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss 'UTC'Z");
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Singapore"));

// Format the Date object to the desired format
        String formattedDate = sdf.format(date);

        Log.d("FormatttedDate val: ",formattedDate);

        return formattedDate;

    }

}
