package com.example.designs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.designs.databinding.DemirbascardtasarimiBinding;

import java.util.List;

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.cardViewTasarimBaglayici> {

    private Context mContext;
    private final List<Asset> demirbasBilgiler; // apiden donen cevap dogrultusunda kullanilacak liste
    private DemirbascardtasarimiBinding tasarim;



    public RVAdapter(Context mContext, List<Asset> demirbasBilgiler) {
        this.mContext = mContext;
        this.demirbasBilgiler = demirbasBilgiler;
    }

    @NonNull
    @Override
    public cardViewTasarimBaglayici onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        tasarim = DemirbascardtasarimiBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
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
        if (usageStatu.equals("active")){
            holder.statu.setImageResource(R.drawable.backgroundactive);
        }
        else {
            holder.statu.setImageResource(R.drawable.backgroundpassive);
        }
        if(kategoriId == 1){
            holder.kategoriIkonu.setImageResource(R.drawable.desktop);
        } else if (kategoriId == 2) {
            holder.kategoriIkonu.setImageResource(R.drawable.desk);
        }
        else if (kategoriId == 3){
            holder.kategoriIkonu.setImageResource(R.drawable.flatware);
        }
        else {
            holder.kategoriIkonu.setImageResource(R.drawable.block);
        }
    }

    @Override
    public int getItemCount() {
        return demirbasBilgiler.size();
    }

    // cardTasarimini baglama

    public class cardViewTasarimBaglayici extends RecyclerView.ViewHolder {

        // cardView uzerinde olan nesneler tanimlanmalidir
        public ImageView kategoriIkonu, statu;
        public TextView kategoriAdi, demirbasAdi;

        public cardViewTasarimBaglayici(DemirbascardtasarimiBinding binding) {
            super(binding.getRoot());
            kategoriIkonu = binding.imageView25;
            statu = binding.imageView15;
            kategoriAdi = binding.textView32;
            demirbasAdi = binding.textView33;
        }
    }

    // RVAdapter içine ekle
    public void updateData(List<Asset> newDemirbasBilgiler) {
        demirbasBilgiler.clear();
        demirbasBilgiler.addAll(newDemirbasBilgiler);
        notifyDataSetChanged();
    }

}

