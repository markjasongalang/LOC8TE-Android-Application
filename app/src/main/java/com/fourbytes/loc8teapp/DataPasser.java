package com.fourbytes.loc8teapp;

public class DataPasser {
    private static String username1;
    private static String username2;
    private static String accountType;
    private static String chatUsername;
    private static String ProUsername;

    private static boolean industryChosen;

    public static String getUsername1() {
        return username1;
    }

    public static void setUsername1(String username1) {
        DataPasser.username1 = username1;
    }

    public static String getUsername2() {
        return username2;
    }

    public static void setUsername2(String username2) {
        DataPasser.username2 = username2;
    }

    public static String getAccountType() {
        return accountType;
    }

    public static void setAccountType(String accountType) {
        DataPasser.accountType = accountType;
    }

    public static String getChatUsername() {
        return chatUsername;
    }

    public static void setChatUsername(String chatUsername) {
        DataPasser.chatUsername = chatUsername;
    }

    public static String getProUsername() {
        return ProUsername;
    }

    public static void setProUsername(String proUsername) {
        ProUsername = proUsername;
    }

    public static boolean isIndustryChosen() {
        return industryChosen;
    }

    public static void setIndustryChosen(boolean industryChosen) {
        DataPasser.industryChosen = industryChosen;
    }
}
