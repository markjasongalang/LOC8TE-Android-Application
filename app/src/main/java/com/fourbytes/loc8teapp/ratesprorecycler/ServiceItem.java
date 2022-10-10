package com.fourbytes.loc8teapp.ratesprorecycler;

public class ServiceItem {
    private String serviceName;
    private double price;
    private String rateType;
    private String description;

    public ServiceItem(String serviceName, double price, String rateType, String description) {
        this.serviceName = serviceName;
        this.price = price;
        this.rateType = rateType;
        this.description = description;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getRateType() {
        return rateType;
    }

    public void setRateType(String rateType) {
        this.rateType = rateType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
