package com.example.designs;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.designs.databinding.FragmentPersonelEkleBinding;

public class PersonelEkleFragment extends Fragment {

    private FragmentPersonelEkleBinding tasarim;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tasarim =  DataBindingUtil.inflate(inflater, R.layout.fragment_personel_ekle, container, false);

        Window window = getActivity().getWindow();;
        window.setStatusBarColor(ContextCompat.getColor(requireContext(),R.color.c3));
        window.setNavigationBarColor(ContextCompat.getColor(requireContext(),R.color.c6));

        tasarim.imageViewGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.popBackStack(); // Bir onceki sayfaya gecis
            }
        });



        return tasarim.getRoot();
    }
}