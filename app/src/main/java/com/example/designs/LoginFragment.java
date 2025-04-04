package com.example.designs;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.designs.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding tasarim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tasarim = DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false);


        tasarim.setLoginFragmentNesnesi(this); // yetki verildi

        Glide.with(getContext())
                .load(R.drawable.logo)  // Resminizi buraya ekleyin
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(50)))  // 16px köşe yarıçapı
                .into(tasarim.imageViewLogo);  // Hedef ImageView'e yükle

        return tasarim.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Binding nesnesini null yaparak bellek sızıntısı engellenir
        tasarim = null;
    }
}