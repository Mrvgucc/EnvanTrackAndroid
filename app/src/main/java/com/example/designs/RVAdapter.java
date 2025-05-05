package com.example.designs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.example.designs.databinding.DemirbascardtasarimiBinding;
import com.example.designs.databinding.PopupwindowforwhichemployeeBinding;
import com.google.android.material.transition.Hold;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.cardViewTasarimBaglayici> {

    private Context mContext;
    private final List<Asset> demirbasBilgiler; // apiden donen cevap dogrultusunda kullanilacak liste
    private DemirbascardtasarimiBinding tasarim;
    private PopupwindowforwhichemployeeBinding popupTasarim;
    private OnEditModeChangeListener listener;

    public void setOnEditModeChangeListener(OnEditModeChangeListener listener) {
        this.listener = listener;
    }

    public RVAdapter(Context mContext, List<Asset> demirbasBilgiler) {
        this.mContext = mContext;
        this.demirbasBilgiler = demirbasBilgiler;
    }

    @NonNull
    @Override
    public cardViewTasarimBaglayici onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        tasarim = DemirbascardtasarimiBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        // tasarim buraya inflate edilir (eklenir)

        return new cardViewTasarimBaglayici(tasarim);
    }

    @Override
    public void onBindViewHolder(@NonNull cardViewTasarimBaglayici holder, int position) {
        Asset asset = demirbasBilgiler.get(position);

        int kategoriId = asset.getCategory_id().getId();
        holder.kategoriAdi.setText(asset.getCategory_id().getName());
        holder.demirbasAdi.setText(asset.getName());
        String usageStatu = asset.getUsage_status();
        if (usageStatu.equals("active")) {
            holder.statu.setImageResource(R.drawable.backgroundactive);
        } else {
            holder.statu.setImageResource(R.drawable.backgroundpassive);
        }
        if (kategoriId == 1) {
            holder.kategoriIkonu.setImageResource(R.drawable.desktop);
        } else if (kategoriId == 2) {
            holder.kategoriIkonu.setImageResource(R.drawable.desk);
        } else if (kategoriId == 3) {
            holder.kategoriIkonu.setImageResource(R.drawable.flatware);
        } else {
            holder.kategoriIkonu.setImageResource(R.drawable.block);
        }

        holder.statu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupTasarim = PopupwindowforwhichemployeeBinding.inflate(LayoutInflater.from(v.getContext()));

                TextView statu = popupTasarim.textView35;
                TextView zimmetliKullanici = popupTasarim.textView34;


                if (usageStatu.equals("active")) {
                    statu.setText("Aktif");
                    String kullaniciAdiSoyadi = "Zimmetli Kullanıcı: " + asset.getRegistered_personal().getName() + " " + asset.getRegistered_personal().getSurname();
                    zimmetliKullanici.setText(kullaniciAdiSoyadi);
                } else {
                    statu.setText("Pasif");
                    // Popup'taki statu'nun margin'ini değiştiriyoruz
                    ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) statu.getLayoutParams();
                    zimmetliKullanici.setVisibility(View.GONE);
                    params.setMargins(10, 0, 10, 20);
                    statu.setLayoutParams(params);
                }

                // popupWindow olusturalim
                PopupWindow popupWindow = new PopupWindow(
                        popupTasarim.getRoot(),
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        true // true : disariya tiklayinca kapanir
                );

                popupWindow.showAsDropDown(v, 0, 10); // gorselin hemen altinda acilir
            }
        });

        if (asset.getIsEditMode()) {

            holder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String updatedAssetName = holder.editText6.getText().toString().trim();
                    String updatedCategoryName = holder.editText5.getText().toString().trim();
                    int categoryId = 0;
                    switch (updatedCategoryName) {
                        case "Teknolojik Ürün":
                            categoryId = 1;
                            break;
                        case "Ofis Mobilyaları":
                            categoryId = 2;
                            break;
                        case "Mutfak Araç Gereçleri":
                            categoryId = 3;
                            break;
                    }

                    DemirbaslGuncelleRequest request = new DemirbaslGuncelleRequest();
                    request.setName(updatedAssetName);
                    request.setCategory_id(categoryId);

                    Islemler islemGuncelleme = new Islemler();
                    islemGuncelleme.demirbasGuncelleme(asset.getId(), request, new Islemler.demirbasGuncellemeCallback() {
                        @Override
                        public void onSuccess(String message) {
                            Toast.makeText(mContext, "Demirbaş başarıyla güncellendi !", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(mContext, "Demirbaş güncelleme başarısız !", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });


            holder.kategoriAdi.setVisibility(View.GONE);
            holder.demirbasAdi.setVisibility(View.GONE);

            holder.editText5.setVisibility(View.VISIBLE);
            holder.editText6.setVisibility(View.VISIBLE);

            holder.check.setVisibility(View.VISIBLE);
            holder.close.setVisibility(View.VISIBLE);


        } else {
            holder.kategoriAdi.setVisibility(View.VISIBLE);
            holder.demirbasAdi.setVisibility(View.VISIBLE);

            holder.editText5.setVisibility(View.GONE);
            holder.editText6.setVisibility(View.GONE);

            holder.check.setVisibility(View.GONE);
            holder.close.setVisibility(View.GONE);
        }

        holder.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                asset.setEditMode(false);
                notifyItemChanged(holder.getAdapterPosition());

                if (listener != null) {
                    listener.onEditChanged(false); // kapatınca haber ver
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return demirbasBilgiler.size();
    }

    // cardTasarimini baglama

    public class cardViewTasarimBaglayici extends RecyclerView.ViewHolder {

        // cardView uzerinde olan nesneler tanimlanmalidir
        public ImageView kategoriIkonu, statu, check, close;
        public TextView kategoriAdi, demirbasAdi;
        public EditText editText5, editText6;

        public cardViewTasarimBaglayici(DemirbascardtasarimiBinding binding) {
            super(binding.getRoot());
            kategoriIkonu = binding.imageView25;
            statu = binding.imageView15;
            kategoriAdi = binding.textView32;
            demirbasAdi = binding.textView33;
            check = binding.imageView31;
            close = binding.imageView32;
            editText5 = binding.editTextText5;
            editText6 = binding.editTextText6;

        }
    }

    // RVAdapter içine ekle
    public void updateData(List<Asset> newDemirbasBilgiler) {
        demirbasBilgiler.clear();
        demirbasBilgiler.addAll(newDemirbasBilgiler);
        notifyDataSetChanged();
    }

    public Asset getItem(int position) {
        return demirbasBilgiler.get(position);
    }

    public void removeItem(int position) {
        demirbasBilgiler.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnEditModeChangeListener {
        void onEditChanged(boolean isEditMode);
    }


}


