package com.example.designs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.designs.databinding.FragmentPersonelZimmetleriBinding;

import java.util.ArrayList;

public class PersonelZimmetleriFragment extends Fragment {

    private FragmentPersonelZimmetleriBinding tasarim;
    private RVAdapterPersonelZimmetleri rvAdapter;
    private PersonelZimmetleriViewModel viewModel;
    private SharedPreferences sp;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tasarim = DataBindingUtil.inflate(inflater, R.layout.fragment_personel_zimmetleri,container,false);

        tasarim.imageView34.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.popBackStack();
            }
        });

        sp = requireActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        String accessToken = sp.getString("accesToken", null);
        String token_type = sp.getString("token_type", null);
        String token = token_type + " " + accessToken;
        int id = sp.getInt("id", 0);


        viewModel = new ViewModelProvider(this).get(PersonelZimmetleriViewModel.class);
        viewModel.zimmetleriGetir(id, token);
        viewModel.getZimmetliDemirbaslar().observe(getViewLifecycleOwner(), assets -> {
            Log.e("Asset listesi", assets.toString());
            rvAdapter = new RVAdapterPersonelZimmetleri(assets, getContext(), viewModel);
            tasarim.recyclerView.setAdapter(rvAdapter);
        });

        viewModel.getTotalAsset().observe(getViewLifecycleOwner(), total -> {
            tasarim.textView44.setText(total + " adet kişisel demirbaşınız listelendi.");
        });

        recyclerViewBagla();


        return tasarim.getRoot();
    }

    private void recyclerViewBagla(){
        try {
            rvAdapter = new RVAdapterPersonelZimmetleri(new ArrayList<>(),getContext(),viewModel);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            tasarim.recyclerView.setLayoutManager(layoutManager);

            // LiveData gözlemi
            viewModel.getZimmetliDemirbaslar().observe(getViewLifecycleOwner(), assets -> {
                Log.e("Asset listesi", assets.toString());
                rvAdapter = new RVAdapterPersonelZimmetleri(assets, getContext(), viewModel);
                tasarim.recyclerView.setAdapter(rvAdapter);
            });
        }
        catch (Exception e){
            Log.e("PersonelZimmetleriFragment", "RecyclerView setup error: " + e.getMessage());
        }

    }
}