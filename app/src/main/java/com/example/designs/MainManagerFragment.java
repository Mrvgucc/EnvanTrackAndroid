package com.example.designs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.designs.databinding.FragmentMainManagerBinding;
import com.example.designs.databinding.FragmentMainPersonalBinding;

public class MainManagerFragment extends Fragment {

    private FragmentMainManagerBinding tasarim;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private EmployeeInfoViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tasarim = DataBindingUtil.inflate(inflater,R.layout.fragment_main_manager, container, false);

        tasarim.setMainManagerNesnesi(this); // xml de olusturdugumuz nesneye yetki verdik

        // olusturlan ViewModel'i baglama islemi:
        viewModel = new ViewModelProvider(this).get(EmployeeInfoViewModel.class);

        Window window = requireActivity().getWindow();
        window.setStatusBarColor(ContextCompat.getColor(requireContext(),R.color.c3));
        window.setNavigationBarColor(ContextCompat.getColor(requireContext(),R.color.c6));

        tasarim.buttonPersonelEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_mainManagerFragment_to_personelEkleFragment);
            }
        });

        // Login olunca sharedPreferences'e kaydedilen tokeni cekelim
        sp = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        String accesToken = sp.getString("accesToken",null); // "token" isimli sharedPreferences dosyasindan "accesToken"a eristik.
        String tokenType = sp.getString("token_type",null);
        String token = tokenType + " " + accesToken ;
        // cekilen bu tokenin kimligini dogrulayip yetkilendirme islemini yapalim
//        Islemler islem2 = new Islemler();
//        islem2.EmployeeInfo(token);
        // buradan donen name, surname, phone, email, status degerlerini cekebilmek icin ViewModel yapisina ihtiyacim var
        viewModel = new ViewModelProvider(this).get(EmployeeInfoViewModel.class);
        viewModel.getEmployeeInfoLiveData().observe(getViewLifecycleOwner(),employeeInfo -> {
            if (employeeInfo != null ){
                tasarim.textView4.setText("Hoşgeldin, " + employeeInfo.getName() + " " + employeeInfo.getSurname());
                tasarim.textView5.setText(employeeInfo.getEmail());
            }
        });

        viewModel.loadEmployeeInfo(token); // veri yukleme





        return tasarim.getRoot();
    }
}