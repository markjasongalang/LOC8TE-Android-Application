package com.fourbytes.loc8teapp.generaleventsrecycler;

public class GeneralEventsItems {

    String event_title;
    String event_location;
    String event_description;
    String event_id;
    String hosted_by;
    String time;
    String date;
    String job_title;
    String host_id;
    int participant_count;
    int parking_limit;
    int parking_count;
    double latitude;
    double longitude;

    public GeneralEventsItems(String event_title, String event_location, String hosted_by, String time, String date, String job_title, String event_id, String host_id, String event_description,
                              int participant_count, int parking_limit, int parking_count, double latitude, double longitude) {
        this.event_title = event_title;
        this.event_location = event_location;
        this.hosted_by = hosted_by;
        this.time = time;
        this.date = date;
        this.job_title = job_title;
        this.event_id = event_id;
        this.host_id = host_id;
        this.event_description = event_description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.participant_count = participant_count;
        this.parking_limit = parking_limit;
        this.parking_count = parking_count;
    }

    public String getEvent_id() {
        return event_id;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public String getEvent_title() {
        return event_title;
    }

    public void setEvent_title(String event_title) {
        this.event_title = event_title;
    }

    public String getEvent_location() {
        return event_location;
    }

    public void setEvent_location(String event_location) {
        this.event_location = event_location;
    }

    public String getHosted_by() {
        return hosted_by;
    }

    public void setHosted_by(String hosted_by) {
        this.hosted_by = hosted_by;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getHost_id() {
        return host_id;
    }

    public void setHost_id(String host_id) {
        this.host_id = host_id;
    }

    public String getEvent_description() {
        return event_description;
    }

    public void setEvent_description(String event_description) {
        this.event_description = event_description;
    }

    public int getParticipant_count() {
        return participant_count;
    }

    public void setParticipant_count(int participant_count) {
        this.participant_count = participant_count;
    }

    public int getParking_limit() {
        return parking_limit;
    }

    public void setParking_limit(int parking_limit) {
        this.parking_limit = parking_limit;
    }

    public int getParking_count() {
        return parking_count;
    }

    public void setParking_count(int parking_count) {
        this.parking_count = parking_count;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}