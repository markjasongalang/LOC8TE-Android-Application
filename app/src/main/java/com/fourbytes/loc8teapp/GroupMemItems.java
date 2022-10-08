package com.fourbytes.loc8teapp;

public class GroupMemItems {
    String groups_name;
    String groups_occupation;
    int groups_image;

    public GroupMemItems(String groups_name, String groups_occupation, int groups_image) {
        this.groups_name = groups_name;
        this.groups_occupation = groups_occupation;
        this.groups_image = groups_image;
    }

    public String getGroups_name() {
        return groups_name;
    }

    public void setGroups_name(String groups_name) {
        this.groups_name = groups_name;
    }

    public String getGroups_occupation() {
        return groups_occupation;
    }

    public void setGroups_occupation(String groups_occupation) {
        this.groups_occupation = groups_occupation;
    }

    public int getGroups_image() {
        return groups_image;
    }

    public void setGroups_image(int groups_image) {
        this.groups_image = groups_image;
    }
}
