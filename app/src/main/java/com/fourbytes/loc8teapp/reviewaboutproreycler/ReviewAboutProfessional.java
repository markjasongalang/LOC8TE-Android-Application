package com.fourbytes.loc8teapp.reviewaboutproreycler;

public class ReviewAboutProfessional {
    private String firstName;
    private String middleName;
    private String lastName;
    private String review;
    private String timestamp;
    private double rating;

    public ReviewAboutProfessional(String firstName, String middleName, String lastName, String review, String timestamp, double rating) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.review = review;
        this.timestamp = timestamp;
        this.rating = rating;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
