package com.example.designs;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EmployeeInfoViewModel extends ViewModel {

    // ViewModel'in amaci : Arayuzu besleyecek verileri organize etmektir
    // ViewModel icerisinde yapilmamasi gerekenler :
    // Toast, SnackBar, Alert gösterilmez
    // Sayfa gecisi icin Intent kullanilamaz
    // Gorsel nesne ile ilgili islemler olamaz
    // Islemsel kodlar yapilmalidir


    // Token sayesinde calisan bilgilerine erisebilecegimiz bir ViewModel yapisi olusturacagiz
    private MutableLiveData<EmployeeInfo> employeeInfoMutableLiveData = new MutableLiveData<>();
    private Islemler islem = new Islemler();

    public LiveData<EmployeeInfo> getEmployeeInfoLiveData(){
        return employeeInfoMutableLiveData;
    }

    public void loadEmployeeInfo(String token) {
        islem.getEmployeeInfo(token, new Islemler.EmployeeInfoCallback() {
            @Override
            public void onSuccess(EmployeeInfo employeeInfo) {
                employeeInfoMutableLiveData.setValue(employeeInfo);
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("ViewModel", "Error: " + errorMessage);
            }
        });
    }


}
