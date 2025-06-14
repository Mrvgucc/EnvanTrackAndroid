package com.example.designs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designs.databinding.PersonelZimmetleriCardTasarimiBinding;

import java.util.List;

public class RVAdapterPersonelZimmetleri extends RecyclerView.Adapter<RVAdapterPersonelZimmetleri.CardViewTasarimBaglayici> {

    private final List<Asset> zimmetliDemirbaslar;
    private final Context mContext;
    private final PersonelZimmetleriViewModel personelZimmetleriViewModel;

    public RVAdapterPersonelZimmetleri(List<Asset> zimmetliDemirbaslar, Context mContext, PersonelZimmetleriViewModel viewModel) {
        this.zimmetliDemirbaslar = zimmetliDemirbaslar;
        this.mContext = mContext;
        this.personelZimmetleriViewModel = viewModel;
    }

    @NonNull
    @Override
    public CardViewTasarimBaglayici onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PersonelZimmetleriCardTasarimiBinding binding = PersonelZimmetleriCardTasarimiBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CardViewTasarimBaglayici(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewTasarimBaglayici holder, int position) {
        Asset asset = zimmetliDemirbaslar.get(position);
        String kategoriAdi = asset.getCategory_id().getName();
        String demirbasAdi = asset.getName();

        holder.binding.textView41.setText(kategoriAdi);
        holder.binding.textView42.setText(demirbasAdi);

        // Kategoriye göre ikon ayarı
        switch (kategoriAdi) {
            case "Teknolojik Ürün":
                holder.binding.imageView45.setImageResource(R.drawable.desktop);
                break;
            case "Ofis Mobilyaları":
                holder.binding.imageView45.setImageResource(R.drawable.desk);
                break;
            case "Mutfak Araç Gereçleri":
                holder.binding.imageView45.setImageResource(R.drawable.flatware);
                break;
            case "Diğer":
                holder.binding.imageView45.setImageResource(R.drawable.block);
                break;
            default:
                holder.binding.imageView45.setImageResource(R.drawable.block);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return zimmetliDemirbaslar.size();
    }

    public static class CardViewTasarimBaglayici extends RecyclerView.ViewHolder {
        public final PersonelZimmetleriCardTasarimiBinding binding;

        public CardViewTasarimBaglayici(PersonelZimmetleriCardTasarimiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
