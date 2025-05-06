package com.example.designs;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface methodInterface {

    @POST("login")
    Call<Login> login(@Body RequestBody body);
    // Call<Login> : retrofit in dondurdugu cevabi temsil eder
    // Call : Retrofit'in kendi sinifidir ve HTTP cagrisini temsil eder.
    // login : metdoumuzun adi
    // @Body : HTTP istegi yapilirken, bu parametrenin requestbody olarak gönderilecegini belirtir
    // @Body : JSON gibi karmasik verileri gonderirken kullanilir
    // RequestBody body : gonderilecek veriyi temsil eder.

    @GET("employeeInfo")
    Call<EmployeeInfo> employeeInfo(@Header("Authorization") String token); // token ile kimlik dogrulama

    @POST("employeeInsert")
    Call<GenericResponse> personelEkle(@Body PersonelEkleRequest request); // personelEkle


    @POST("employeeSearchWithName")
    Call<PersonelAramaResponse> personelSearchWithName(@Body personelSilRequest request);

    @POST("employeeSearchWithId")
    Call<PersonelAramaResponse> personelSearchWithId(@Body personelSilRequest2 request);

    @GET("assetList")
    Call<DemirbasListlemeResponse> demirbasListeleme();

    @DELETE("assetDelete/{id}")
    Call<DemirbasSilmeResponse> demirbasSilme(@Path("id") int id);

    @PUT("assetUpdate/{id}")
    Call<DemirbasGuncellemeResponse> demirbasGuncelleme(@Path("id") int id ,@Body DemirbaslGuncelleRequest request);

    @GET("categoryList")
    Call<CategoryListResponse> kategoriListeleme();

    @GET("employeeList")
    Call<EmployeeListResponse> calisanListeleme();

}
