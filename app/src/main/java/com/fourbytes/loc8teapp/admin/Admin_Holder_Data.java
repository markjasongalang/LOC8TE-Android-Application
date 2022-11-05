package com.fourbytes.loc8teapp.admin;

public class Admin_Holder_Data {

    String professional_name;
    int image;

    public Admin_Holder_Data(String professional_name, int image) {
        this.professional_name = professional_name;
        this.image = image;
    }


    public String getProfessional_name() {
        return professional_name;
    }

    public int getImage() {
        return image;
    }
}
