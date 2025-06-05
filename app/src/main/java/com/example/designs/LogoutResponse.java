package com.example.designs;

import com.google.gson.annotations.SerializedName;

public class LogoutResponse {

    @SerializedName("message")
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
