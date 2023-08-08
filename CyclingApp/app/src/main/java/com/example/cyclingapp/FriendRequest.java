package com.example.cyclingapp;

public class FriendRequest {

    private String senderID;
    private String receiverID;
    private String description;


    public FriendRequest(){
        senderID = receiverID = description = null;
    }

    public FriendRequest(String senderID,String receiverID,String description){
        this.senderID = senderID;
        this.receiverID = receiverID;
        this.description = description;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public void setReceiverID(String receiverID){
        this.receiverID = receiverID;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getReceiverID() {
        return receiverID;
    }

    public String getSenderID() {
        return senderID;
    }
}
