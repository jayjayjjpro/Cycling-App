package com.example.cyclingapp;

public class Friend {

    private String username;
    private String ID;

    public Friend() {
    }

    public Friend(String username, String ID) {
        this.username = username;
        this.ID = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
