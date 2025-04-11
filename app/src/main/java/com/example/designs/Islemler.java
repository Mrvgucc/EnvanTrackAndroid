package com.example.designs;

import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Islemler {

    private static final methodInterface methodInterface = APIUtils.getMethodInterface();

    public void login(String email, String password, final LoginCallback callback) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("email", email);
        jsonObject.addProperty("password", password);

        Log.e("İstek", "Giden JSON: " + jsonObject.toString());

        RequestBody body = RequestBody.create(
                MediaType.parse("application/json"),
                jsonObject.toString()
        );

        methodInterface.login(body).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Login loginData = response.body();
                    String accessToken = loginData.getAccessToken();
                    Log.e("Başarılı", "Token Türü: " + loginData.getTokenType());
                    Log.e("Başarılı", "Access Token: " + accessToken);

                    callback.onTokenReceived(accessToken, loginData);
                } else {
                    Log.e("Hata", "Yanıt Kodu: " + response.code());
                    try {
                        if (response.errorBody() != null) {
                            Log.e("Hata", "Hata Mesajı: " + response.errorBody().string());
                        } else {
                            Log.e("Hata", "Sunucu'dan beklenmeyen bir yanıt alındı.");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    callback.onError("Hata: Yanıt alınamadı");
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Log.e("Hata", "İstek başarısız oldu: " + t.getMessage());
                callback.onError("Hata: " + t.getMessage());
            }

        });
    }

    public interface LoginCallback {
        void onTokenReceived(String accessToken, Login login);

        void onError(String errorMessage);
    }

}
