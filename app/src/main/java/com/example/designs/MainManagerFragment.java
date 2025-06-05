package com.example.designs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Toast;

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
        tasarim = DataBindingUtil.inflate(inflater, R.layout.fragment_main_manager, container, false);

        tasarim.setMainManagerNesnesi(this); // xml de olusturdugumuz nesneye yetki verdik

        // olusturlan ViewModel'i baglama islemi:
        viewModel = new ViewModelProvider(this).get(EmployeeInfoViewModel.class);
        tasarim.setEmployeeInfo(viewModel);

        tasarim.setLifecycleOwner(getViewLifecycleOwner()); // LiveData guncellenince XML otomatik degissin

        Window window = requireActivity().getWindow();
        window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.c3));
        window.setNavigationBarColor(ContextCompat.getColor(requireContext(), R.color.c6));

        tasarim.imageView13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_mainManagerFragment_to_demirbasIslemleriFragment);
            }
        });

        tasarim.imageView14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_mainManagerFragment_to_personelIslemlerFragment);
            }
        });

        // Login olunca sharedPreferences'e kaydedilen tokeni cekelim
        sp = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        String accesToken = sp.getString("accesToken", null); // "token" isimli sharedPreferences dosyasindan "accesToken"a eristik.
        String tokenType = sp.getString("token_type", null);
        String token = tokenType + " " + accesToken;
        // cekilen bu tokenin kimligini dogrulayip yetkilendirme islemini yapalim
//        Islemler islem2 = new Islemler();
//        islem2.EmployeeInfo(token);

        viewModel.loadEmployeeInfo(token); // veri yukleme


        // logout islemi
        tasarim.imageView41.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Çıkış")
                        .setMessage("Uygulamadan çıkış yapmak istediğinize emin misiniz?")
                        .setPositiveButton("Evet", (dialog, which) -> {
                            Islemler cikisIslemi = new Islemler();
                            cikisIslemi.cikisYap(token, new Islemler.cikisCallBack() {
                                @Override
                                public void onSuccess(String message) {
                                    Toast.makeText(getContext(),"Çıkış işlemi başarılı.", Toast.LENGTH_SHORT).show();
                                    Navigation.findNavController(v).navigate(R.id.action_mainManagerFragment_to_loginFragment2);


                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(getContext(),"Çıkış işlemi başarısız.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        })
                        .setNegativeButton("Hayır", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setIcon(R.drawable.exclamation)
                        .show();

            }
        });

        return tasarim.getRoot();
    }
}