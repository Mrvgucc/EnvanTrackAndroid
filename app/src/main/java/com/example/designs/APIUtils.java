package com.example.designs;

public class APIUtils {

    public static final String BASE_URL = "http://192.168.1.149:8000/api/";
    public static methodInterface getMethodInterface(){
        return RetrofitClient.getClient(BASE_URL).create(methodInterface.class);
    }
}
