package com.example.designs;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.example.designs.databinding.FragmentMainManagerBinding;
import com.example.designs.databinding.FragmentMainPersonalBinding;

public class MainManagerFragment extends Fragment {

    private FragmentMainManagerBinding tasarim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tasarim = DataBindingUtil.inflate(inflater,R.layout.fragment_main_manager, container, false);

        Window window = requireActivity().getWindow();
        window.setStatusBarColor(ContextCompat.getColor(requireContext(),R.color.c3));
        window.setNavigationBarColor(ContextCompat.getColor(requireContext(),R.color.c6));

        tasarim.buttonPersonelEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_mainManagerFragment_to_personelEkleFragment);
            }
        });


        return tasarim.getRoot();
    }
}