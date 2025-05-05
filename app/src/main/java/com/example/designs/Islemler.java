package com.example.designs;

import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.security.PrivateKey;

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

    public void getEmployeeInfo(String token, final EmployeeInfoCallback callback) {
        methodInterface.employeeInfo(token).enqueue(new Callback<EmployeeInfo>() {
            @Override
            public void onResponse(Call<EmployeeInfo> call, Response<EmployeeInfo> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onError("Yanıt alınamadı: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<EmployeeInfo> call, Throwable t) {
                callback.onError("İstek başarısız: " + t.getMessage());
            }
        });
    }

    public interface EmployeeInfoCallback {
        void onSuccess(EmployeeInfo employeeInfo);

        void onError(String errorMessage);
    }

    public void personelEkle(PersonelEkleRequest request, final personelEkleCallBack callBack) {
        methodInterface.personelEkle(request).enqueue(new Callback<GenericResponse>() {
            @Override
            public void onResponse(Call<GenericResponse> call, Response<GenericResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callBack.onSuccess(response.body().getMessage());
                } else {
                    callBack.onError("Yanıt Alınamadı: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<GenericResponse> call, Throwable t) {
                callBack.onError("İstek başarısız: " + t.getMessage());
            }
        });
    }

    public interface personelEkleCallBack {
        void onSuccess(String message); // GenericResponse icerisinde sadece message oldugu icin sadece message gonderildi.

        void onError(String errorMessage);
    }

    public void demirbasSilme(int id, SilmeCallback callback) {
        methodInterface api = APIUtils.getMethodInterface();
        Call<DemirbasSilmeResponse> call = api.demirbasSilme(id);
        String message = "";

        call.enqueue(new Callback<DemirbasSilmeResponse>() {
            @Override
            public void onResponse(Call<DemirbasSilmeResponse> call, Response<DemirbasSilmeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("Silme İslemi", response.body().getMessage());
                    callback.onSuccess(response.body().getMessage());
                } else {
                    callback.onError("Yanit alinamadi");
                }
            }

            @Override
            public void onFailure(Call<DemirbasSilmeResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });

    }

    public interface SilmeCallback {
        void onSuccess(String message);

        void onError(String error);
    }

    public void demirbasGuncelleme(int id,DemirbaslGuncelleRequest request, final demirbasGuncellemeCallback callback) {
        methodInterface.demirbasGuncelleme(id,request).enqueue(new Callback<DemirbasGuncellemeResponse>() {
            @Override
            public void onResponse(Call<DemirbasGuncellemeResponse> call, Response<DemirbasGuncellemeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getMessage());
                }
                else {
                    callback.onError("Yanıt Alınamadı: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<DemirbasGuncellemeResponse> call, Throwable t) {
                callback.onError("İstek başarısız: " + t.getMessage());
            }
        });
    }

    public interface demirbasGuncellemeCallback {
        void onSuccess(String message);

        void onError(String error);
    }


}


