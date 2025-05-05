package com.example.designs;

import static androidx.core.util.TypedValueCompat.dpToPx;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.designs.databinding.DialogDemribasGuncelleBinding;
import com.example.designs.databinding.FragmentDemirbasIslemleriBinding;

import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DemirbasIslemleriFragment extends Fragment {

    private FragmentDemirbasIslemleriBinding tasarim;
    private RVAdapter adapter;
    private ProgressBar progressBar;
    private SwipeRefreshLayout swipeRefreshLayout;
    private DialogDemribasGuncelleBinding tasarim2;

    private int dpToPx(int dp) {
        float density = requireContext().getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

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

        adapter.setOnEditModeChangeListener(new RVAdapter.OnEditModeChangeListener() {
            @Override
            public void onEditChanged(boolean isEditMode) {
                if (isEditMode) {
                    tasarim.view2.setVisibility(View.INVISIBLE);
                } else {
                    tasarim.view2.setVisibility(View.GONE);
                }
            }
        });


        loadData();


        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Asset asset = adapter.getItem(position);

                if (direction == ItemTouchHelper.RIGHT) {
                    Islemler islemDemirbasSilme = new Islemler();
                    int demirbasId = asset.getId();
                    islemDemirbasSilme.demirbasSilme(demirbasId, new Islemler.SilmeCallback() {
                        @Override
                        public void onSuccess(String message) {
                            adapter.removeItem(position);
                            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                            loadData();
                        }

                        @Override
                        public void onError(String error) {
                            adapter.notifyItemChanged(position); // geri al
                            Toast.makeText(getContext(), "Silme hatası: " + error, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else if (direction == ItemTouchHelper.LEFT) {
                    // update işlemi yapılabilir
                    asset = adapter.getItem(position);

                    asset.setEditMode(true);
                    adapter.notifyItemChanged(position); // UI gunceller
                }
            }


            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                View itemView = viewHolder.itemView;
                int itemHeight = itemView.getHeight();
                Paint paint = new Paint();

                if (dX > 0) { // sağa kaydırma
                    paint.setColor(ContextCompat.getColor(requireContext(), R.color.kirmizi));
                    c.drawRect((float) itemView.getLeft(), (float) itemView.getTop(), dX, (float) itemView.getBottom(), paint);

                    Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.delete);
                    if (icon != null) {
                        int iconSize = dpToPx(24);
                        int iconTop = itemView.getTop() + (itemHeight - iconSize) / 2;
                        int iconMargin = dpToPx(16);
                        int iconLeft = itemView.getLeft() + iconMargin;
                        int iconRight = iconLeft + iconSize;
                        int iconBottom = iconTop + iconSize;

                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);
                    }
                } else if (dX < 0) { // sola kaydırma
                    paint.setColor(ContextCompat.getColor(requireContext(), R.color.mavi));
                    c.drawRect((float) itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom(), paint);

                    Drawable icon = ContextCompat.getDrawable(requireContext(), R.drawable.edit);
                    if (icon != null) {
                        int iconSize = dpToPx(24);
                        int iconTop = itemView.getTop() + (itemHeight - iconSize) / 2;
                        int iconMargin = dpToPx(16);
                        int iconRight = itemView.getRight() - iconMargin;
                        int iconLeft = iconRight - iconSize;
                        int iconBottom = iconTop + iconSize;

                        icon.setBounds(iconLeft, iconTop, iconRight, iconBottom);
                        icon.draw(c);
                    }
                }

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        new ItemTouchHelper(simpleCallback).attachToRecyclerView(tasarim.rv);


        return tasarim.getRoot();
    }

    public void loadData() {

        progressBar.setVisibility(View.VISIBLE); // ProgressBar gorunur olur

        // API cagrisi
        final methodInterface methodInterface = APIUtils.getMethodInterface();

        methodInterface.demirbasListeleme().

                enqueue(new Callback<DemirbasListlemeResponse>() {
                    @Override
                    public void onResponse(Call<DemirbasListlemeResponse> call, Response<DemirbasListlemeResponse> response) {
                        if (response.body() != null) {
                            Log.e("API_RESPONSE", "Gelen veri: " + response.body().getAssets().size() + " adet asset");
                            adapter.updateData(response.body().getAssets());
                            int demirbasSayisi = response.body().getAssets().size();
                            String adet = demirbasSayisi + " adet demirbaş listelendi.";
                            tasarim.textView36.setText(adet);

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

        tasarim.imageView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController = Navigation.findNavController(v);
                navController.popBackStack();
            }
        });


        tasarim.imageView26.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_demirbasIslemleriFragment_to_personelEkleFragment);
            }
        });

    }
}