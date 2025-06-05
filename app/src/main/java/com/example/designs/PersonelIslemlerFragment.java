package com.example.designs;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

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
import android.widget.Toast;

import com.example.designs.databinding.FragmentPersonelIslemlerBinding;

import java.util.ArrayList;

public class PersonelIslemlerFragment extends Fragment {

    private FragmentPersonelIslemlerBinding tasarim;
    private RVAdapterPersonal adapterPersonal;
    private PersonelViewModel personelViewModel;
    private Handler mainHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tasarim = DataBindingUtil.inflate(inflater, R.layout.fragment_personel_islemler, container, false);
        mainHandler = new Handler(Looper.getMainLooper());

        initViewModel();
        setupRecyclerView();
        setupObservers();
        setupClickListeners();

        // Load data asynchronously
        loadDataSafely();

        return tasarim.getRoot();
    }

    private void initViewModel() {
        personelViewModel = new ViewModelProvider(requireActivity()).get(PersonelViewModel.class);
    }

    private void setupRecyclerView() {
        try {
            adapterPersonal = new RVAdapterPersonal(getContext(), new ArrayList<>(), personelViewModel);

            LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
            tasarim.rvPersonal.setLayoutManager(layoutManager);
            tasarim.rvPersonal.setAdapter(adapterPersonal);

            // Performance optimizations
            tasarim.rvPersonal.setItemViewCacheSize(20);
            tasarim.rvPersonal.setDrawingCacheEnabled(true);
            tasarim.rvPersonal.setHasFixedSize(true);

        } catch (Exception e) {
            Log.e("PersonelFragment", "RecyclerView setup error: " + e.getMessage());
        }
    }

    private void setupObservers() {
        // PersonelListesi observer
        personelViewModel.getPersonelListesi().observe(getViewLifecycleOwner(), personelList -> {
            // Main thread'de UI güncellemesi yap
            if (mainHandler != null) {
                mainHandler.post(() -> {
                    updatePersonelList(personelList);
                });
            } else {
                updatePersonelList(personelList);
            }
        });

        // Mesaj observer
        personelViewModel.getMesaj().observe(getViewLifecycleOwner(), mesaj -> {
            if (mainHandler != null) {
                mainHandler.post(() -> {
                    handleMessage(mesaj);
                });
            } else {
                handleMessage(mesaj);
            }
        });

        personelViewModel.getSilmeMesaji().observe(getViewLifecycleOwner(), mesaj -> {
            if (mainHandler != null) {
                mainHandler.post(() -> {
                    handleMessagForDeleteOption(mesaj);
                });
            } else {
                handleMessagForDeleteOption(mesaj);
            }
        });
    }

    private void updatePersonelList(java.util.List<Employee> personelList) {
        try {
            if (personelList != null && adapterPersonal != null) {
                adapterPersonal.setData(personelList);

                // UI elementlerini güncelle
                if (tasarim != null) {
                    hideLoadingIndicators();
                    updatePersonelCount(personelList.size());
                }
            } else {
                // Null durumunda boş liste göster
                if (adapterPersonal != null) {
                    adapterPersonal.setData(new ArrayList<>());
                }
                updatePersonelCount(0);
            }
        } catch (Exception e) {
            Log.e("PersonelFragment", "Error updating list: " + e.getMessage());
            showErrorToast("Liste güncellenirken hata oluştu");
        }
    }

    private void hideLoadingIndicators() {
        tasarim.view7.setVisibility(View.INVISIBLE);
        tasarim.progressBar2.setVisibility(View.INVISIBLE);
    }

    private void updatePersonelCount(int count) {
        tasarim.textView40.setText(count + " personel listelendi.");
    }

    private void handleMessage(String mesaj) {
        try {
            if (mesaj != null) {
                if ("true".equals(mesaj)) {
                    showSuccessToast("Güncelleme işlemi başarılı.");
                    // Başarılı güncelleme sonrası veriyi yenile
                    loadDataSafely();
                } else if ("false".equals(mesaj)) {
                    showErrorToast("Güncelleme işlemi başarısız!");
                }
                personelViewModel.temizleMesaj();
            }
        } catch (Exception e) {
            Log.e("PersonelFragment", "Error handling message: " + e.getMessage());
        }
    }

    public void handleMessagForDeleteOption(String mesaj){
        try {
            if (mesaj != null) {
                if ("true".equals(mesaj)) {
                    showSuccessToast("Silme işlemi başarılı.");
                    // Başarılı silme sonrası veriyi yenile
                    loadDataSafely();
                } else if ("false".equals(mesaj)) {
                    showErrorToast("Silme işlemi başarısız!");
                }
                personelViewModel.temizleMesaj();
            }
        } catch (Exception e) {
            Log.e("PersonelFragment", "Error handling message: " + e.getMessage());
        }
    }

    private void setupClickListeners() {
        // Geri tuşu
        tasarim.imageView34.setOnClickListener(v -> {
            try {
                NavController controller = Navigation.findNavController(v);
                controller.popBackStack();
            } catch (Exception e) {
                Log.e("PersonelFragment", "Navigation error: " + e.getMessage());
            }
        });

        // Personel ekleme
        tasarim.imageView35.setOnClickListener(v -> {
            try {
                Navigation.findNavController(v)
                        .navigate(R.id.action_personelIslemlerFragment_to_personelEkleFragment);
            } catch (Exception e) {
                Log.e("PersonelFragment", "Navigation error: " + e.getMessage());
            }
        });
    }

    private void loadDataSafely() {
        if (personelViewModel == null) {
            Log.e("PersonelFragment", "ViewModel is null, cannot load data");
            return;
        }

        // Loading indicator'ları göster
        showLoadingIndicators();

        // Background thread'de veri yükle
        Thread loadingThread = new Thread(() -> {
            try {
                // Fragment hala aktif mi kontrol et
                if (isAdded() && personelViewModel != null) {
                    personelViewModel.loadPersonel();
                }
            } catch (Exception e) {
                Log.e("PersonelFragment", "Error loading data: " + e.getMessage());
                if (mainHandler != null && isAdded()) {
                    mainHandler.post(() -> {
                        hideLoadingIndicators();
                        showErrorToast("Veriler yüklenirken hata oluştu");
                    });
                }
            }
        });
        loadingThread.setName("PersonelDataLoader");
        loadingThread.start();
    }

    private void showLoadingIndicators() {
        if (tasarim != null) {
            if (tasarim.view7 != null) {
                tasarim.view7.setVisibility(View.VISIBLE);
            }
            if (tasarim.progressBar2 != null) {
                tasarim.progressBar2.setVisibility(View.VISIBLE);
            }
        }
    }

    private void showSuccessToast(String message) {
        if (isAdded() && getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    private void showErrorToast(String message) {
        if (isAdded() && getContext() != null) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Fragment visible olduğunda veriyi yenile
        if (personelViewModel != null && isAdded()) {
            // Kısa bir delay ile yükle (UI'nin hazır olmasını bekle)
            if (mainHandler != null) {
                mainHandler.postDelayed(() -> {
                    if (isAdded() && personelViewModel != null) {
                        loadDataSafely();
                    }
                }, 100); // 100ms delay
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // Fragment invisible olduğunda handler'ı temizle
        if (mainHandler != null) {
            mainHandler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Memory leak'leri önle
        try {
            if (mainHandler != null) {
                mainHandler.removeCallbacksAndMessages(null);
                mainHandler = null;
            }

            // RecyclerView adapter'ını temizle
            if (tasarim != null && tasarim.rvPersonal != null) {
                tasarim.rvPersonal.setAdapter(null);
            }

            // References'ları temizle
            adapterPersonal = null;
            personelViewModel = null;
            tasarim = null;

        } catch (Exception e) {
            Log.e("PersonelFragment", "Error in onDestroyView: " + e.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // Final cleanup
        mainHandler = null;
        adapterPersonal = null;
        personelViewModel = null;
        tasarim = null;
    }
}