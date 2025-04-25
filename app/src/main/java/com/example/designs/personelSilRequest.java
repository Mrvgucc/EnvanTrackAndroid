package com.example.designs;

import com.google.gson.annotations.SerializedName;

public class personelSilRequest {


    @SerializedName("name")
    private String name;

    @SerializedName("surname")
    private String surname;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
