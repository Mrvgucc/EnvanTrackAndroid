package com.example.designs;

import com.google.gson.annotations.SerializedName;

public class RegisteredPersonel {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("surname")
    private String surname;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

}
