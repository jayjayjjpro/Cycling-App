package com.example.cyclingapp;

import java.util.Date;
import java.util.List;

public class Events {
    private String id;
    private String name;
    private Date startTime;
    private String location;
    private String creatorId;
    private List<String> participants; //ID from Users

    //Constructor
    public Events(String id, String name, Date startTime, String location, String creatorId, List<String> participants) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.location = location;
        this.creatorId = creatorId;
        this.participants = participants;
    }

    //Getter and Setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }
}
