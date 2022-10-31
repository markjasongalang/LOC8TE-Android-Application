package com.fourbytes.loc8teapp.fragment;

public class MarkerTag {

    private String id;
    private String type;
    private String name;
    private String job;

    public MarkerTag(String id, String type){
        this.id = id;
        this.type = type;
    }

    public MarkerTag(String id, String type, String name, String job){
        this.id = id;
        this.type = type;
        this.name = name;
        this.job = job;
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
}
