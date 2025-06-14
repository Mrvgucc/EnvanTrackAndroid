package com.example.designs;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class PersonelZimmetleriViewModel  extends ViewModel {

    private MutableLiveData<List<Asset>> zimmetliDemirbaslar = new MutableLiveData<>();
    private MutableLiveData<Integer> tumAsset = new MutableLiveData<>();
    private SharedPreferences sp;

    public LiveData<List<Asset>> getZimmetliDemirbaslar(){
        return zimmetliDemirbaslar;
    }

    public LiveData<Integer> getTotalAsset(){
        return tumAsset;
    }

    public void zimmetleriGetir(int personalId, String token){
        Islemler personelZimmetleri = new Islemler();
        personelZimmetleri.assetListWithId(token,personalId, new Islemler.AssetListWithIdCallback() {
            @Override
            public void onSucces(List<Asset> assets, int totalAsset) {
                for (Asset asset : assets){
                    String demirbasAdi = asset.getName();
                    String kategoriAdi = asset.getCategory_id().getName();
                    Log.e("ZimmetViewModel" , "Demirbas: " + demirbasAdi + "Kategori: " + kategoriAdi );
                }

                // LiveData guncelleme
                zimmetliDemirbaslar.setValue(assets);
                tumAsset.setValue(totalAsset);
            }

            @Override
            public void onError(String error) {
                Log.e("ZimmetViewModel", "API çağrısı başarısız: " + error);
            }
        });
    }
}
