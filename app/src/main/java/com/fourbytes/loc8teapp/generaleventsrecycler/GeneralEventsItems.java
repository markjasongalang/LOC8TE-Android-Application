package com.fourbytes.loc8teapp.generaleventsrecycler;

public class GeneralEventsItems {

    String event_title;
    String event_location;
    String hosted_by;
    String time;
    String date;
    String job_title;
    int image;

    public GeneralEventsItems(String event_title, String event_location, String hosted_by, String time, String date, String job_title, int image) {
        this.event_title = event_title;
        this.event_location = event_location;
        this.hosted_by = hosted_by;
        this.time = time;
        this.date = date;
        this.job_title = job_title;
        this.image = image;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

}