package com.example.designs;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.designs.databinding.FragmentPersonelEkleBinding;
import com.example.designs.databinding.FragmentPersonelSilBinding;

public class PersonelSilFragment extends Fragment {

    private FragmentPersonelSilBinding tasarim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tasarim =  DataBindingUtil.inflate(inflater,R.layout.fragment_personel_sil, container, false);



        return tasarim.getRoot();
    }
}