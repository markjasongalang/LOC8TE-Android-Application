package com.fourbytes.loc8teapp.experienceprorecycler;

public class ExperienceItem {
    private String position;
    private String company;
    private String description;

    public ExperienceItem(String position, String company, String description) {
        this.position = position;
        this.company = company;
        this.description = description;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
