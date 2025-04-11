package com.example.designs;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface methodInterface {

    @POST("login")
    Call<Login> login(@Body RequestBody body);
    // Call<Login> : retrofit in dondurdugu cevabi temsil eder
    // Call : Retrofit'in kendi sinifidir ve HTTP cagrisini temsil eder.
    // login : metdoumuzun adi
    // @Body : HTTP istegi yapilirken, bu parametrenin requestbody olarak gönderilecegini belirtir
    // @Body : JSON gibi karmasik verileri gonderirken kullanilir
    // RequestBody body : gonderilecek veriyi temsil eder.
}
