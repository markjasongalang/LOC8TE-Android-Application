package com.fourbytes.loc8teapp.fragment;

import com.google.android.gms.maps.model.LatLng;

public class MarkerTag {

    private String id;
    private String type;
    private String name;
    private String job;
    private String field;
    private LatLng location;

    public MarkerTag(String id, String type){
        this.id = id;
        this.type = type;
    }

    public MarkerTag(String id, String type, String name, String job, String field, LatLng location){
        this.id = id;
        this.type = type;
        this.name = name;
        this.job = job;
        this.field = field;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public String getField() {
        return field;
    }

    public LatLng getLocation() {
        return location;
    }
}
