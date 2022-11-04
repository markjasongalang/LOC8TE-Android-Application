package com.fourbytes.loc8teapp;

public class Pair {
    private String username;
    private String accountType;
    private String name;
    private String field;
    private String specific_job;

    public Pair(String username, String accountType) {
        this.username = username;
        this.accountType = accountType;
    }

    public Pair(String username, String accountType, String name) {
        this.username = username;
        this.accountType = accountType;
        this.name = name;
    }

    public Pair(String username, String accountType, String name, String field, String specific_job) {
        this.username = username;
        this.accountType = accountType;
        this.name = name;
        this.field = field;
        this.specific_job = specific_job;
    }

    public String getUsername() {
        return username;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getName() {
        return name;
    }

    public String getField() {
        return field;
    }

    public String getSpecific_job() {
        return specific_job;
    }
}
