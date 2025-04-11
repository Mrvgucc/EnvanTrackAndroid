package com.example.designs;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.designs.databinding.FragmentMainPersonalBinding;

public class MainPersonalFragment extends Fragment {

    private FragmentMainPersonalBinding tasarim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            tasarim = DataBindingUtil.inflate(inflater,R.layout.fragment_main_personal, container, false);

        Window window = requireActivity().getWindow();
        window.setStatusBarColor(ContextCompat.getColor(requireContext(),R.color.c3));
        window.setNavigationBarColor(ContextCompat.getColor(requireContext(),R.color.c6));




            return tasarim.getRoot();


    }
}