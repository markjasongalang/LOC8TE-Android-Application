package com.fourbytes.loc8teapp.reviewaboutproreycler;

public class ReviewAboutProfessional {
    private String clientName;
    private double rating;
    private String review;
    private String timestamp;

    public ReviewAboutProfessional(String clientName, double rating, String review, String timestamp) {
        this.clientName = clientName;
        this.rating = rating;
        this.review = review;
        this.timestamp = timestamp;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
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
