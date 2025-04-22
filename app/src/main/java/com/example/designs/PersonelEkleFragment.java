package com.example.designs;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.designs.databinding.FragmentPersonelEkleBinding;

public class PersonelEkleFragment extends Fragment {

    private FragmentPersonelEkleBinding tasarim;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private boolean gozAcik = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        tasarim = DataBindingUtil.inflate(inflater, R.layout.fragment_personel_ekle, container, false);

        tasarim.setPersonelEkleNesnesi(this); // xml deki nesneyi kurduk.

        Window window = getActivity().getWindow();
        ;
        window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.c3));
        window.setNavigationBarColor(ContextCompat.getColor(requireContext(), R.color.c6));

        tasarim.imageViewGeri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.popBackStack(); // Bir onceki sayfaya gecis
            }
        });


        tasarim.buttonPersonelEkleOnay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String gelenAd = tasarim.editTextGelenAd.getText().toString().trim();
                String gelenSoyad = tasarim.editTextGelenSoyad.getText().toString().trim();
                String gelenEmail = tasarim.editTextGelenEmail.getText().toString().trim();
                String gelenTelefon = tasarim.editTextGelenTelefonNo.getText().toString().trim();
                String sifre = tasarim.editTextGelenSifre.getText().toString().trim();
                String sifreTekrari = tasarim.editTextGelenSifreTekrar.getText().toString().trim();
//                if (tasarim.radioButtonGelenYonetici.isChecked()) {
//                    status = "manager";
//                } else {
//                    status = "personal";
//                }
                String status = tasarim.radioButtonGelenYonetici.isChecked() ? "manager" : "personal";


                if (!sifre.equals(sifreTekrari)) {
                    Toast.makeText(getContext(), "Şifreler aynı olmalı!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(gelenEmail).matches()) {
                    Toast.makeText(getContext(), "Geçerli bir email adresi giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (gelenAd.isEmpty()) {
                    Toast.makeText(getContext(), "Adınızı giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (gelenSoyad.isEmpty()) {
                    Toast.makeText(getContext(), "Soyadınızı giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (gelenEmail.isEmpty()) {
                    Toast.makeText(getContext(), "Emailinizi giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (gelenTelefon.isEmpty()) {
                    Toast.makeText(getContext(), "Telefon numaranızı giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!tasarim.radioButtonGelenPersonel.isChecked() && !tasarim.radioButtonGelenYonetici.isChecked()) {
                    Toast.makeText(getContext(), "Lütfen bir rol seçiniz (Yönetici veya Personel).", Toast.LENGTH_SHORT).show();
                    return;
                } else if (sifre.isEmpty()) {
                    Toast.makeText(getContext(), "Şifrenizi giriniz.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (sifreTekrari.isEmpty()){
                    Toast.makeText(getContext(), "Şifrenizi tekrar giriniz.", Toast.LENGTH_SHORT).show();
                }

                PersonelEkleRequest request = new PersonelEkleRequest();
                request.setName(gelenAd);
                request.setSurname(gelenSoyad);
                request.setEmail(gelenEmail);
                request.setPhone(gelenTelefon);
                request.setPassword(sifre);
                request.setStatus(status);

                Islemler islemPersonelEkle = new Islemler();
                islemPersonelEkle.personelEkle(request, new Islemler.personelEkleCallBack() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(getContext(), "Personel başarıyla eklendi !", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e("Ekleme Basarisiz" , errorMessage);
                        Toast.makeText(getContext(), "Personel ekleme işlemi başarısız !", Toast.LENGTH_LONG).show();
                    }
                });
            }


        });




        return tasarim.getRoot();
    }

    public void gozDegistirme(){
        ImageView imageViewGoz = tasarim.imageView17;
        ImageView imageViewGoz2 = tasarim.imageView23;
        EditText editTextSifre = tasarim.editTextGelenSifre;
        EditText editTextSifre2 = tasarim.editTextGelenSifreTekrar;

        // imageViewGoz'e tiklandiginda
        if(gozAcik){ // goz acik ise
            editTextSifre.setTransformationMethod(new android.text.method.PasswordTransformationMethod()); // sifre gorunmez olur
            editTextSifre2.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
            imageViewGoz.setImageResource(R.drawable.visibility);
            imageViewGoz2.setImageResource(R.drawable.visibility);
        }
        else {
            editTextSifre.setTransformationMethod(null); // sifre gorunur olur
            editTextSifre2.setTransformationMethod(null);
            imageViewGoz.setImageResource(R.drawable.visibilityoff);
            imageViewGoz2.setImageResource(R.drawable.visibilityoff);
        }

        gozAcik = !gozAcik;

        editTextSifre.setSelection(editTextSifre.getText().length()); // imleci sona tasima
        editTextSifre2.setSelection(editTextSifre2.getText().length());
    }
}