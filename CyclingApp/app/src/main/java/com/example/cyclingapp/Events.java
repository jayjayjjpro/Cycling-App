package com.example.cyclingapp;

import android.util.Log;

import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

public class Events {

    private String id;
    private String name;
    private Date startTime;
    private String location;
    private String creatorId;
    private List<String> participants; //ID from Users
    private List<SubLatLng> eventLatLngLst = null;

    private Status status;

    private double estimatedDistanceInKM;


    enum Status{
        COMPLETED,
        ONGOING,
        NOTSTARTED,
        STARTED
    }

    //Constructor
    public Events() {
        // Default constructor required for calls to DataSnapshot.getValue(Events.class)
    }
    public Events(String id, String name, Date startTime, String location, String creatorId,
                  List<String> participants, Status status) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.location = location;
        this.creatorId = creatorId;
        this.participants = participants;
        this.status = status;
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

    public boolean checkParticipantDuplicate(String participant){
        for (int i=0;i<participants.size();i++){
            if (participants.get(i).equals(participant)){
                return true;
            }
        }
        return false;
    }


    public void addParticipants(String participant){
        if (checkParticipantDuplicate(participant)==false) this.participants.add(participant);
    }

    public List<SubLatLng> getEventLatLngLst(){return eventLatLngLst;}

    public void setEventLatLngLst(List<SubLatLng>eventLatLngLst){ this.eventLatLngLst = eventLatLngLst;}

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setEstimatedDistanceInKM(double estimatedDistance){
        this.estimatedDistanceInKM = estimatedDistance;
    }

    public double getEstimatedDistanceInKM(){
        return this.estimatedDistanceInKM;
    }
}
