package com.example.designs;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

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
import java.util.List;

public class DemirbasEkleFragment extends Fragment {

    private FragmentDemirbasEkleBinding tasarim;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tasarim = DataBindingUtil.inflate(inflater, R.layout.fragment_demirbas_ekle, container, false);

        Islemler islem = new Islemler();
        AutoCompleteTextView autoCompleteTextView = tasarim.autoCompleteTextView;
        islem.kategoriListeleme(new Islemler.demirbasListelemeCallback() {
            @Override
            public void onCategoriesLoaded(List<Category> categories) {
                List<String> categoryName = new ArrayList<>();
                for (Category c: categories){
                    categoryName.add(c.getName());
                }

                //Adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        categoryName
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                autoCompleteTextView.setAdapter(adapter);

            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(requireContext(), "Kategori yüklenemedi: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        AutoCompleteTextView autoCompleteTextView2 = tasarim.autoCompleteTextView2;
        islem.calisanListeleme(new Islemler.calisanListelemeCallBack() {
            @Override
            public void onEmployeesLoaded(List<Employee> employees) {
                List<String> employeeList = new ArrayList<>();
                for (Employee e : employees){
                    employeeList.add(e.getName() + e.getSurname());
                }
                //Adapter
                ArrayAdapter<String> adapter = new ArrayAdapter<>(
                        requireContext(),
                        android.R.layout.simple_spinner_item,
                        employeeList
                );
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                autoCompleteTextView2.setAdapter(adapter);
            }

            @Override
            public void onError(String errorMessage) {

            }
        });

        return tasarim.getRoot();
    }
}