package com.example.designs;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.designs.databinding.FragmentPersonelIslemlerBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonelIslemlerFragment extends Fragment {

    private FragmentPersonelIslemlerBinding tasarim;
    private RVAdapterPersonal adapterPersonal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        tasarim = DataBindingUtil.inflate(inflater,R.layout.fragment_personel_islemler, container, false);

        tasarim.rvPersonal.setLayoutManager(new LinearLayoutManager(getContext()));

        // adapter baglama
        adapterPersonal =  new RVAdapterPersonal(getContext(), new ArrayList<>(),tasarim);
        tasarim.rvPersonal.setAdapter(adapterPersonal);


        loadData();

        tasarim.imageView35.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_personelIslemlerFragment_to_personelEkleFragment);
            }
        });


        return tasarim.getRoot();
    }

    public void loadData(){

        final methodInterface methodInterface = APIUtils.getMethodInterface();
        methodInterface.calisanListeleme().enqueue(new Callback<EmployeeListResponse>() {
            @Override
            public void onResponse(Call<EmployeeListResponse> call, Response<EmployeeListResponse> response) {
                if (response.body() != null) {
                    Log.e("API_RESPONSE", "Gelen veri: " + response.body().getEmployees().size() + " adet personel");
                    adapterPersonal.setData(response.body().getEmployees());

                    int personelSayisi = response.body().getEmployees().size();
                    String adet = personelSayisi + " adet personel listelendi.";
                    tasarim.textView40.setText(adet);

                } else {
                    Log.d("API_RESPONSE", "Gelen veri null!");
                }
            }

            @Override
            public void onFailure(Call<EmployeeListResponse> call, Throwable t) {
                Log.e("API_ERROR", "Hata oluştu: " + t.getMessage());
            }
        });

        tasarim.imageView34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.popBackStack();
            }
        });
    }


}