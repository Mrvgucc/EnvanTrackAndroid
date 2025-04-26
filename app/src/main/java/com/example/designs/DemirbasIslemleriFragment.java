package com.example.designs;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.designs.databinding.FragmentDemirbasIslemleriBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DemirbasIslemleriFragment extends Fragment {

    private FragmentDemirbasIslemleriBinding tasarim;
    private RVAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tasarim = DataBindingUtil.inflate(inflater, R.layout.fragment_demirbas_islemleri, container, false);

        swipeRefreshLayout = tasarim.swipeRefreshLayout;
        progressBar = tasarim.progressBar;

        tasarim.rv.setLayoutManager(new LinearLayoutManager(getContext()));

        adapter = new RVAdapter(getContext(), new ArrayList<>());
        tasarim.rv.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
            }
        });

        loadData();

        return tasarim.getRoot();
    }

    public void loadData() {

        progressBar.setVisibility(View.VISIBLE); // ProgressBar gorunur olur

        // API cagrisi
        final methodInterface methodInterface = APIUtils.getMethodInterface();

        methodInterface.demirbasListeleme().

                enqueue(new Callback<DemirbasListlemeResponse>() {
                    @Override
                    public void onResponse
                            (Call<DemirbasListlemeResponse> call, Response<DemirbasListlemeResponse> response) {
                        if (response.body() != null) {
                            Log.e("API_RESPONSE", "Gelen veri: " + response.body().getAssets().size() + " adet asset");
                            adapter.updateData(response.body().getAssets());
                        } else {
                            Log.d("API_RESPONSE", "Gelen veri null!");
                        }

                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                    }



                    @Override
                    public void onFailure(Call<DemirbasListlemeResponse> call, Throwable t) {
                        Log.e("API_ERROR", "Hata oluştu: " + t.getMessage());

                        // Yenileme islemi tamamlandiginda SwipeRefresh ve ProgressBar i yukle
                        swipeRefreshLayout.setRefreshing(false);
                        progressBar.setVisibility(View.GONE);
                    }
                });


    }
}