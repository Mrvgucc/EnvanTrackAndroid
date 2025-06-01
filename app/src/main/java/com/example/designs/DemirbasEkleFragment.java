package com.example.designs;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.ArrayMap;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.designs.databinding.FragmentDemirbasEkleBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DemirbasEkleFragment extends Fragment {

    private FragmentDemirbasEkleBinding tasarim;
    private Category secilenKategori = null;
    private Employee secilenCalisan = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        tasarim = DataBindingUtil.inflate(inflater, R.layout.fragment_demirbas_ekle, container, false);
        Islemler listeleme = new Islemler();

        AutoCompleteTextView autoCompleteTextView = tasarim.autoCompleteTextView;
        listeleme.kategoriListeleme(new Islemler.demirbasListelemeCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
                ArrayAdapter<Category> adapter = new ArrayAdapter<>(
                        requireContext(), android.R.layout.simple_dropdown_item_1line, categories
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                autoCompleteTextView.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(requireContext(), "Kategoriler yüklenemedi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        tasarim.autoCompleteTextView.setOnItemClickListener((parent, view, position, id) ->
                secilenKategori = (Category) parent.getItemAtPosition(position));

        AutoCompleteTextView autoCompleteTextView2 = tasarim.autoCompleteTextView2;
        listeleme.calisanListeleme(new Islemler.calisanListelemeCallBack() {
            @Override
            public void onEmployeesLoaded(List<Employee> employees) {
                ArrayAdapter<Employee> adapter = new ArrayAdapter<>(
                        requireContext(), android.R.layout.simple_dropdown_item_1line, employees
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                autoCompleteTextView2.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(requireContext(), "Çalışanlar yüklenemedi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        tasarim.autoCompleteTextView2.setOnItemClickListener((parent, view, position, id) ->
                secilenCalisan = (Employee) parent.getItemAtPosition(position));


        tasarim.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String gelenDemirbasAdi = tasarim.textInputDemirbasAdi.getText().toString();

                if (gelenDemirbasAdi.isEmpty()) {
                    tasarim.textInputLayout.setErrorEnabled(true);
                    tasarim.textInputLayout.setError("Bu alan boş bırakılamaz.");

                    int redColor = getResources().getColor(R.color.kirmizi, null);
                    tasarim.textInputLayout.setBoxStrokeColor(redColor);

                    return; // Diğer işlemleri durdur
                } else {
                    tasarim.textInputLayout.setError(null);
                }

                if(secilenKategori == null){
                    int redColor = getResources().getColor(R.color.kirmizi, null);
                    tasarim.textInputLayout2.setErrorEnabled(true);
                    tasarim.textInputLayout2.setError("Lütfen bir kategori seçin.");
                    tasarim.textInputLayout2.setBoxStrokeColor(redColor);
                    return;
                }else {
                    tasarim.textInputLayout2.setError(null);

                }

                int gelenKatedoriId = secilenKategori.getId();
                Integer gelenKullaniciId = null;

                if (secilenCalisan != null) {
                    gelenKullaniciId = secilenCalisan.getId();
                }

                DemirbasEkleRequest request = new DemirbasEkleRequest();
                request.setName(gelenDemirbasAdi);
                request.setCatefory_id(gelenKatedoriId);
                request.setEmployeeId(gelenKullaniciId);

                Islemler islemDemirbasEkle = new Islemler();
                islemDemirbasEkle.demirbasEkle(request, new Islemler.demirbasEkleCallback() {
                    @Override
                    public void onSuccess(String message) {
                        Toast.makeText(getContext(), "Demirbaş başarıyla eklendi !", Toast.LENGTH_LONG).show();
                        tasarim.textInputDemirbasAdi.setText("");
                        tasarim.autoCompleteTextView.setText("");
                        tasarim.autoCompleteTextView2.setText("");
                        secilenCalisan = null;
                    }

                    @Override
                    public void onError(String errorMessage) {
                        Toast.makeText(getContext(), "Demirbaş ekleme işlemi başarısız !", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

        tasarim.imageView28.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            }
        });

        return tasarim.getRoot();
    }
}