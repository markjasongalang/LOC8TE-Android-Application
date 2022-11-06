package com.fourbytes.loc8teapp.experienceprorecycler;

public class ExperienceItem {
    private String experienceId;
    private String position;
    private String company;
    private String description;

    public ExperienceItem(String experienceId, String position, String company, String description) {
        this.experienceId = experienceId;
        this.position = position;
        this.company = company;
        this.description = description;
    }

    public String getExperienceId() {
        return experienceId;
    }

    public void setExperienceId(String experienceId) {
        this.experienceId = experienceId;
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
