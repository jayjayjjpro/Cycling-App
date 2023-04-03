package com.example.cyclingapp;

import java.util.List;

public class User {
    private String id;
    private String displayName;
    private String email;

    private List<String> completedEvents;

    private List<String> joinedEvents;


    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String id, String displayName, String email) {
        this.id = id;
        this.displayName = displayName;
        this.email = email;
        this.completedEvents = completedEvents;
        this.joinedEvents = joinedEvents;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getCompletedEvents() {
        return completedEvents;
    }

    public void setCompletedEvents(List<String> completedEvents) {
        this.completedEvents = completedEvents;
    }

    public List<String> getJoinedEvents() {
        return joinedEvents;
    }

    public void setJoinedEvents(List<String> joinedEvents) {
        this.joinedEvents = joinedEvents;
    }

    public void addJoinedEvents(String newJoinEvent){this.joinedEvents.add(newJoinEvent);}
}
