package com.example.cyclingapp;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

import java.io.Serializable;
import java.util.List;

public class ChatRoomModel implements Parcelable {
    String chatroomID;
    List<String> userIDs;
    Timestamp lastMessageTimestamp;
    String lastMessageSenderID;

    String lastMessageSenderUsername;



    public ChatRoomModel(String chatroomID, List<String> userIDs, Timestamp lastMessageTimestamp, String lastMessageSenderID, String lastMessageSenderUsername) {
        this.chatroomID = chatroomID;
        this.userIDs = userIDs;
        this.lastMessageTimestamp = lastMessageTimestamp;
        this.lastMessageSenderID = lastMessageSenderID;
        this.lastMessageSenderUsername = lastMessageSenderUsername;
    }

    public ChatRoomModel() {
    }


    protected ChatRoomModel(Parcel in) {
        chatroomID = in.readString();
        userIDs = in.createStringArrayList();
        lastMessageTimestamp = in.readParcelable(Timestamp.class.getClassLoader());
        lastMessageSenderID = in.readString();
        lastMessageSenderUsername = in.readString();
    }

    public static final Creator<ChatRoomModel> CREATOR = new Creator<ChatRoomModel>() {
        @Override
        public ChatRoomModel createFromParcel(Parcel in) {
            return new ChatRoomModel(in);
        }

        @Override
        public ChatRoomModel[] newArray(int size) {
            return new ChatRoomModel[size];
        }
    };

    public String getChatroomID() {
        return chatroomID;
    }

    public List<String> getUserIDs() {
        return userIDs;
    }

    public Timestamp getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public String getLastMessageSenderID() {
        return lastMessageSenderID;
    }

    public void setChatroomID(String chatroomID) {
        this.chatroomID = chatroomID;
    }

    public void setUserIDs(List<String> userIDs) {
        this.userIDs = userIDs;
    }

    public void setLastMessageTimestamp(Timestamp lastMessageTimestamp) {
        this.lastMessageTimestamp = lastMessageTimestamp;
    }

    public void setLastMessageSenderID(String lastMessageSenderID) {
        this.lastMessageSenderID = lastMessageSenderID;
    }

    public String getLastMessageSenderUsername() {
        return lastMessageSenderUsername;
    }

    public void setLastMessageSenderUsername(String lastMessageSenderUsername) {
        this.lastMessageSenderUsername = lastMessageSenderUsername;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(chatroomID);
        dest.writeStringList(userIDs);
        dest.writeParcelable(lastMessageTimestamp, flags);
        dest.writeString(lastMessageSenderID);
        dest.writeString(lastMessageSenderUsername);
    }
}
