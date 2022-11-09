package com.fourbytes.loc8teapp.reviewforprorecycler;

public class ReviewForProfessional {
    private String professionalName;
    private double rating;
    private String review;
    private String timestamp;

    public ReviewForProfessional(String professionalName, double rating, String review, String timestamp) {
        this.professionalName = professionalName;
        this.rating = rating;
        this.review = review;
        this.timestamp = timestamp;
    }

    public String getProfessionalName() {
        return professionalName;
    }

    public void setProfessionalName(String professionalName) {
        this.professionalName = professionalName;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
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
}
