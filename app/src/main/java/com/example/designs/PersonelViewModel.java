package com.example.designs;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonelViewModel extends ViewModel {
    private MutableLiveData<List<Employee>> personelListesi = new MutableLiveData<>();
    private MutableLiveData<String> silmeMesaji = new MutableLiveData<>();
    private MutableLiveData<Integer> getPozition = new MutableLiveData<>();
    private MutableLiveData<String> mesaj = new MutableLiveData<>();
    private methodInterface methodInterface = APIUtils.getMethodInterface();
    private Context mContext;

    public LiveData<List<Employee>> getPersonelListesi() {
        return personelListesi;
    }

    public LiveData<String> getSilmeMesaji() {
        return silmeMesaji;
    }

    public LiveData<Integer> silinenPozition() {
        return getPozition;
    }

    public LiveData<String> getMesaj(){
        return mesaj; // bu mesaj fragmnet ta observe ile dinlenir
    }



    public void loadPersonel() { // personellerin listelenmesini saglayan loadPersonel() metodu
        methodInterface.calisanListeleme().enqueue(new Callback<EmployeeListResponse>() {
            @Override
            public void onResponse(Call<EmployeeListResponse> call, Response<EmployeeListResponse> response) {
                if (response.body() != null) {
                    personelListesi.setValue(response.body().getEmployees()); // personelListesi apiden donen yanit sonucunda personlleri tutar
                }
            }

            @Override
            public void onFailure(Call<EmployeeListResponse> call, Throwable t) {
                Log.e("VM", "Hata" + t.getMessage());
            }
        });
    }


    public void personelGuncelle(int id, PersonelGuncelleRequest request){
        methodInterface.personelGuncelle(id,request).enqueue(new Callback<PersonelGuncelleResponse>() {
            @Override
            public void onResponse(Call<PersonelGuncelleResponse> call, Response<PersonelGuncelleResponse> response) {
                mesaj.setValue("true");
            }

            @Override
            public void onFailure(Call<PersonelGuncelleResponse> call, Throwable t) {
                mesaj.setValue("false");
            }
        });
    }

    public void temizleMesaj() {
        mesaj.setValue(""); // veya null da olabilir, ikisi de olur
    }

    public void personelSil(int id){
        methodInterface.personelSilme(id).enqueue(new Callback<PersonelSilmeRespone>() {
            @Override
            public void onResponse(Call<PersonelSilmeRespone> call, Response<PersonelSilmeRespone> response) {
                if (response.isSuccessful()){
                    silmeMesaji.setValue("true");
                }
                else {
                    silmeMesaji.setValue("false");
                }
            }

            @Override
            public void onFailure(Call<PersonelSilmeRespone> call, Throwable t) {
                silmeMesaji.setValue("false");
            }
        });
    }

    // veri guncelleme islemi
    // personelEkleme islemi
}
