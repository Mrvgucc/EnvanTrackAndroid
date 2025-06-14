package com.example.designs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.designs.databinding.FragmentMainPersonalBinding;

import java.util.List;

public class MainPersonalFragment extends Fragment {

    private FragmentMainPersonalBinding tasarim;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private EmployeeInfoViewModel viewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tasarim = DataBindingUtil.inflate(inflater, R.layout.fragment_main_personal, container, false);

        viewModel = new ViewModelProvider(this).get(EmployeeInfoViewModel.class);
        tasarim.setEmployeeInfo(viewModel); // viewModeli XML'e bagladik
        tasarim.setLifecycleOwner(getViewLifecycleOwner()); // LiveData’nın otomatik çalışması için


        Window window = requireActivity().getWindow();
        window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.c3));
        window.setNavigationBarColor(ContextCompat.getColor(requireContext(), R.color.c6));

        // SharedPrefernces ile login olmus kullanicinin tokenini tutalim boylelikle tokenden de kullanici verilerini cekelim
        sharedPreferences = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("accesToken", null);
        String token_type = sharedPreferences.getString("token_type", null);
        String token = token_type + " " + accessToken;
        int id = sharedPreferences.getInt("id", 0);
        Log.e("id", String.valueOf(id));

        viewModel.loadEmployeeInfo(token);

        Islemler islemAssetListelemeId = new Islemler();
        islemAssetListelemeId.assetListWithId(token, id, new Islemler.AssetListWithIdCallback() {
            @Override
            public void onSucces(List<Asset> assets, int totalAsset) {
                tasarim.textView12.setText(totalAsset + " Adet");
                Log.e("Toplam Zimmet Adedi : ", String.valueOf(totalAsset));
            }

            @Override
            public void onError(String error) {
                Log.e("Asset Listeleme Hatasi : ", error);
            }
        });

        Islemler demirbasListeleme = new Islemler();
        demirbasListeleme.demirbasListeleme(new Islemler.demirbasListelemeCallBack() {
            @Override
            public void onSuccess(List<Asset> assets, int totalAsset) {
                tasarim.textView14.setText(totalAsset + " Adet");
            }

            @Override
            public void onError(String error) {

            }
        });

        tasarim.imageView42.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_mainPersonalFragment_to_personelZimmetleriFragment);
            }
        });


        return tasarim.getRoot();


    }
}