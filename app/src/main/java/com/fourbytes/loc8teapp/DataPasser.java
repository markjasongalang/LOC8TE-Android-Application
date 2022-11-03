package com.fourbytes.loc8teapp;

public class DataPasser {
    private static String username;
    private static String accountType;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        DataPasser.username = username;
    }

    public static String getAccountType() {
        return accountType;
    }

    public static void setAccountType(String accountType) {
        DataPasser.accountType = accountType;
    }
}
