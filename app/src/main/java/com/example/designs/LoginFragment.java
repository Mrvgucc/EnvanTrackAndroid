package com.example.designs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.RenderEffect;
import android.graphics.Shader;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.example.designs.databinding.FragmentLoginBinding;

public class LoginFragment extends Fragment {

    private FragmentLoginBinding tasarim;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    private boolean gozAcik = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        tasarim = DataBindingUtil.inflate(inflater,R.layout.fragment_login, container, false);


        Window window = requireActivity().getWindow();
        window.setStatusBarColor(ContextCompat.getColor(requireContext(), R.color.c1)); // status bar rengini arka plan rengi ile ayni yapma
        window.setNavigationBarColor(ContextCompat.getColor(requireContext(), R.color.c1)); // navigation bar rengini arka plan rengi ile ayni yapma


        tasarim.setLoginFragmentNesnesi(this); // fragment data icerisinde olusturdugumuz nesnenin calisabilmesi icin burada yetki verilir.
        // this ile de bunu buradaki fragmentta kullancagim anlamina gelir

        Glide.with(getContext())
                .load(R.drawable.logo)  // Resminizi buraya ekleyin
                .apply(RequestOptions.bitmapTransform(new RoundedCorners(50 )))  // 16px köşe yarıçapı
                .into(tasarim.imageViewLogo);  // Hedef ImageView'e yükle

        // -----------------------------------------------------------------------------------------


        tasarim.buttonGiris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = tasarim.editTextEmail.getText().toString();
                String password = tasarim.editTextPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()) {
                    Log.e("Login Error", "Kullanıcı adı veya şifre boş olamaz.");
                    Toast.makeText(getContext(), "Kullanıcı adı veya şifre boş olamaz!",Toast.LENGTH_LONG).show();
                    return;  // Hatalı giriş durumu
                }

                ProgressBar progressBar = tasarim.progressBar;
                View view = tasarim.view;
                progressBar.setVisibility(View.VISIBLE);
                if(progressBar.getVisibility() == View.VISIBLE){
                    view.setVisibility(View.VISIBLE);
                }


                Log.e("email" , email);
                Log.e("password" , password);

                Islemler islemLogin = new Islemler();
                islemLogin.login(email, password, new Islemler.LoginCallback() {
                    @Override
                    public void onTokenReceived(String accessToken, Login login) {
                        sp = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
                        editor = sp.edit();
                        editor.putString("accesToken" , accessToken);
                        editor.putString("token_type", login.getTokenType());
                        editor.putInt("id", login.getId());
                        editor.putString("name", login.getName());
                        editor.putString("surname", login.getSurname());
                        editor.putString("email", login.getEmail());
                        editor.putString("status", login.getStatus());
                        editor.putString("phone",login.getPhone());
                        editor.commit();
                        Log.e("Token Preferences a kaydedildi" , accessToken);
                        Log.e("Ad Preferences a kaydedildi" , login.getName());
                        tasarim.progressBar.setVisibility (View.GONE);
                        view.setVisibility(View.GONE);
                        if (login.getStatus().equals("personal")){
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment2_to_mainPersonalFragment);
                        }
                        else if (login.getStatus().equals("manager")){
                            Navigation.findNavController(v).navigate(R.id.action_loginFragment2_to_mainManagerFragment);
                        }
                        // login basarili oldugu zaman diger ekran yuklenene kadar circular progressbar ekranda gorunsun

                    }

                    @Override
                    public void onError(String errorMessage) {
                        Log.e("Login Error onError", errorMessage);
                        tasarim.progressBar.setVisibility(View.GONE);
                        view.setVisibility(View.GONE);
                        AlertDialog.Builder alert = new AlertDialog.Builder(requireContext());
                        alert.setTitle("Uyarı ");
                        alert.setMessage("Kullanıcı adı veya şifre hatalı!");
                        alert.setIcon(R.drawable.errror);
                        alert.setCancelable(true);
                            alert.setNegativeButton("Tamam", (dialog, which) -> {
                                dialog.dismiss();
                            });

                        AlertDialog alertDialog = alert.create();
                        alertDialog.show();


                        new Handler(Looper.getMainLooper()).postDelayed(() -> {
                            if (alertDialog.isShowing()){
                                alertDialog.dismiss();
                            }
                        }, 3000);

                    }
                });
            }
        });




        return tasarim.getRoot();
    }

    public void gozDegistirme(){

        ImageView goz = tasarim.imageViewGoz;
        EditText sifre = tasarim.editTextPassword;
        if (gozAcik){
            sifre.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
            goz.setImageResource(R.drawable.visibility);

        }
        else {
            sifre.setTransformationMethod(null); // veya new HideReturnsTransformationMethod()
            goz.setImageResource(R.drawable.visibilityoff);

        }
        gozAcik = !gozAcik; // durum degistir

        // İmleci sona taşı (goz resmi değiştiğinde imleç başa gitmesin diye bu işlem yapilir)
        sifre.setSelection(sifre.getText().length());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Binding nesnesini null yaparak bellek sızıntısı engellenir
        tasarim = null;
    }


}