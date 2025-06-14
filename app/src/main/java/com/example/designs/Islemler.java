package com.example.designs;

import android.util.Log;

import com.google.gson.JsonObject;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

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

    public void demirbasGuncelleme(int id, DemirbaslGuncelleRequest request, final demirbasGuncellemeCallback callback) {
        methodInterface.demirbasGuncelleme(id, request).enqueue(new Callback<DemirbasGuncellemeResponse>() {
            @Override
            public void onResponse(Call<DemirbasGuncellemeResponse> call, Response<DemirbasGuncellemeResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getMessage());
                } else {
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

    public void kategoriListeleme(kategoriListelemeCallback callback) {
        methodInterface.kategoriListeleme().enqueue(new Callback<CategoryListResponse>() {
            @Override
            public void onResponse(Call<CategoryListResponse> call, Response<CategoryListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onCategoriesLoaded(response.body().getCategories());
                } else {
                    callback.onError("Yanıt Basarisiz" + response.code());
                }
            }

            @Override
            public void onFailure(Call<CategoryListResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface kategoriListelemeCallback {
        void onCategoriesLoaded(List<Category> categories);

        void onError(String errorMessage);
    }


    public void calisanListeleme(calisanListelemeCallBack callback) {
        methodInterface.calisanListeleme().enqueue(new Callback<EmployeeListResponse>() {
            @Override
            public void onResponse(Call<EmployeeListResponse> call, Response<EmployeeListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onEmployeesLoaded(response.body().getEmployees());
                } else {
                    callback.onError("Yanit alinamadi" + response.code());
                }
            }

            @Override
            public void onFailure(Call<EmployeeListResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface calisanListelemeCallBack {
        void onEmployeesLoaded(List<Employee> employees);

        void onError(String errorMessage);
    }

    public void demirbasEkle(DemirbasEkleRequest request, demirbasEkleCallback callback) {
        methodInterface.demirbasEkle(request).enqueue(new Callback<DemirbasEkleResponse>() {
            @Override
            public void onResponse(Call<DemirbasEkleResponse> call, Response<DemirbasEkleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getMessage());
                } else {
                    callback.onError("Yanit alinamadi" + response.code());
                }
            }

            @Override
            public void onFailure(Call<DemirbasEkleResponse> call, Throwable t) {
                callback.onError("Istek Basarisiz" + t.getMessage());
            }
        });
    }

    public interface demirbasEkleCallback {
        void onSuccess(String message);

        void onError(String errorMessage);
    }

    public void personelSil(int id, personelSilmeCallback callback) {
        methodInterface api = APIUtils.getMethodInterface();
        Call<PersonelSilmeRespone> call = api.personelSilme(id);

        call.enqueue(new Callback<PersonelSilmeRespone>() {
            @Override
            public void onResponse(Call<PersonelSilmeRespone> call, Response<PersonelSilmeRespone> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.e("Silme İslemi", response.body().getMessage());
                    callback.onSuccess(response.body().getMessage());
                } else {
                    callback.onError("Yanit alinamadi");
                }
            }

            @Override
            public void onFailure(Call<PersonelSilmeRespone> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface personelSilmeCallback {
        void onSuccess(String message);

        void onError(String error);
    }

    public void personelGuncelle(int id, PersonelGuncelleRequest request, final personelGuncelleCallback callback) {
        methodInterface.personelGuncelle(id, request).enqueue(new Callback<PersonelGuncelleResponse>() {
            @Override
            public void onResponse(Call<PersonelGuncelleResponse> call, Response<PersonelGuncelleResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body().getMessage());
                } else {
                    callback.onError("Yanıt Alınamadı: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<PersonelGuncelleResponse> call, Throwable t) {
                callback.onError("İstek başarısız: " + t.getMessage());
            }
        });
    }

    public interface personelGuncelleCallback {
        void onSuccess(String message);

        void onError(String error);
    }


    public void cikisYap(String accessToken, final cikisCallBack callBack) {
        methodInterface.cikisYap(accessToken).enqueue(new Callback<LogoutResponse>() {
            @Override
            public void onResponse(Call<LogoutResponse> call, Response<LogoutResponse> response) {
                if (response.isSuccessful()) {
                    String message = response.body().getMessage();
                    callBack.onSuccess(message); // callback ile mesaj gosterme
                } else {
                    callBack.onError("Çıkış başarısız: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<LogoutResponse> call, Throwable t) {
                callBack.onError("Sunucu hatası: " + t.getLocalizedMessage());
            }
        });
    }

    public interface cikisCallBack {
        void onSuccess(String message);

        void onError(String error);
    }

    public void assetListWithId(String token, int id, AssetListWithIdCallback callback){
        methodInterface.assetListWithResponse(token,id).enqueue(new Callback<AssetListWithIdResponse>() {
            @Override
            public void onResponse(Call<AssetListWithIdResponse> call, Response<AssetListWithIdResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    callback.onSucces(response.body().getAssets(),response.body().getCount());
                }
                else{
                    callback.onError("Yanıt başarısız." + response.code());
                }
            }

            @Override
            public void onFailure(Call<AssetListWithIdResponse> call, Throwable t) {
                callback.onError(t.getMessage());
            }
        });
    }

    public interface AssetListWithIdCallback{
            void onSucces(List<Asset> assets,int totalAsset);
        void onError(String error);
    }


    public void demirbasListeleme(demirbasListelemeCallBack callBack){
        methodInterface.demirbasListeleme().enqueue(new Callback<DemirbasListlemeResponse>() {
            @Override
            public void onResponse(Call<DemirbasListlemeResponse> call, Response<DemirbasListlemeResponse> response) {
                if (response.isSuccessful() && response.body()!= null){
                    callBack.onSuccess(response.body().getAssets(),response.body().getCount());
                    Log.e("Asset Sayisi: " , String.valueOf(response.body().getCount()));
                    Log.e("Asset listesi" , String.valueOf(response.body().getAssets()));
                }
                else {
                    callBack.onError("Yanit basariiz:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<DemirbasListlemeResponse> call, Throwable t){
                callBack.onError(t.getMessage());
            }
        });
    }

    public interface demirbasListelemeCallBack{
        void onSuccess(List<Asset> assets, int totalAsset);
        void onError(String error);
    }


}


