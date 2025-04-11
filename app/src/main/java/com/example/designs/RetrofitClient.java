package com.example.designs;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static Retrofit retrofit = null;
    public static Retrofit getClient(String baseUrl){
        if(retrofit == null){
            OkHttpClient client = new OkHttpClient.Builder()
                    .build();

            Gson gson = new GsonBuilder()
                    .setLenient()  // JSON'u daha esnek bir şekilde okumasını sağlar
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(client)  // OkHttpClient'ı ekliyoruz
                    .addConverterFactory(GsonConverterFactory.create(gson)) // Gson konverter ile JSON'u parse ediyoruz
                    .build();
        }

        return retrofit;
    }
}
