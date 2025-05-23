package com.example.designs;

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
import com.example.designs.databinding.FragmentPersonelSilBinding;

public class PersonelSilFragment extends Fragment {

    private FragmentPersonelSilBinding tasarim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tasarim =  DataBindingUtil.inflate(inflater,R.layout.fragment_personel_sil, container, false);

        String id = tasarim.editTextText.getText().toString();
        String adSoyad = tasarim.editTextText2.getText().toString();

        Window window = getActivity().getWindow();
        window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.c3));
        window.setNavigationBarColor(ContextCompat.getColor(requireContext(), R.color.c6));

        tasarim.imageView24.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.popBackStack(); // Bir onceki sayfaya gecis
            }
        });

        return tasarim.getRoot();
    }
}