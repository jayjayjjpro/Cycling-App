package com.example.cyclingapp;

import com.google.firebase.Timestamp;

public class ChatMessage {

    private String message;
    private String ID;
    private String senderID;

    private Timestamp timestamp;



    public ChatMessage(){

    }

    public ChatMessage(String message, String ID, String senderID, Timestamp timestamp) {
        this.message = message;
        this.ID = ID;
        this.senderID = senderID;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
