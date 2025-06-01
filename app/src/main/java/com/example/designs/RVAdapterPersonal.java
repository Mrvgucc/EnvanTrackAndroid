package com.example.designs;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designs.databinding.FragmentPersonelIslemlerBinding;
import com.example.designs.databinding.PersonelcardtasarimiBinding;
import com.example.designs.databinding.UpdatePersonDialogBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class RVAdapterPersonal extends RecyclerView.Adapter<RVAdapterPersonal.cardNesneleriTutucu> {

    private Context mContext;
    private List<Employee> personelListesi;
    private FragmentPersonelIslemlerBinding personelIslemlerBinding;

    public RVAdapterPersonal(Context mContext, List<Employee> personelListesi, FragmentPersonelIslemlerBinding personelIslemlerBinding) {
        this.mContext = mContext;
        this.personelListesi = personelListesi;
        this.personelIslemlerBinding = personelIslemlerBinding;
    }

    @NonNull
    @Override
    public cardNesneleriTutucu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // tasarim buraya eklenir (card Tasarimi)
        PersonelcardtasarimiBinding binding = PersonelcardtasarimiBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new cardNesneleriTutucu(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull cardNesneleriTutucu holder, int position) { // listedeki kacinci elemandir
        Employee employee = personelListesi.get(position); // yani position icerisinde kacinci sira varsa o employee i belirtir

        int personelId = employee.getId();
        holder.binding.textView41.setText(employee.getName());
        holder.binding.textView42.setText(employee.getSurname());
        holder.binding.textView43.setText(employee.getEmail());
        holder.binding.textView44.setText(employee.getPhone());
        holder.binding.textView46.setText(employee.getStatus());
        holder.binding.imageView37.setImageResource(R.drawable.person);

        String adSoyad = employee.getName() + " " + employee.getSurname();

        holder.imageView38.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext()).
                        setTitle("Kullanıcıyı Sil")
                        .setMessage("\"" + adSoyad + "\" adlı kullanıcıyı silmek istediğinize emin misiniz ?")
                        .setPositiveButton("Evet", (dialog, which) -> {
                            // silme islemi burada yapilir
                            Islemler personelSil = new Islemler();
                            personelSil.personelSil(personelId, new Islemler.personelSilmeCallback() {
                                @Override
                                public void onSuccess(String message) {
                                    Toast.makeText(v.getContext(), message, Toast.LENGTH_SHORT).show();

                                    // silinen kullaniciyi rv den de cikartarak guncellik saglanir
                                    personelListesi.remove(employee);
                                    notifyItemRemoved(holder.getAdapterPosition());
                                    notifyItemRangeChanged(holder.getAdapterPosition(), personelListesi.size());
                                }

                                @Override
                                public void onError(String error) {
                                    Toast.makeText(v.getContext(), "Silme hatası: " + error, Toast.LENGTH_SHORT).show();

                                }
                            });
                        })
                        .setNegativeButton("Hayır", (dialog, which) -> {
                            dialog.dismiss();
                        })
                        .setCancelable(true) // geri tuşuyla kapanmasın
                        .show();
            }
        });

        holder.imageView40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                personelIslemlerBinding.view6.setVisibility(View.VISIBLE);

                holder.binding.editTextText11.setVisibility(View.VISIBLE);
                holder.binding.editTextText12.setVisibility(View.VISIBLE);
                holder.binding.editTextText13.setVisibility(View.VISIBLE);
                holder.binding.editTextText14.setVisibility(View.VISIBLE);
                holder.binding.editTextText15.setVisibility(View.VISIBLE);
                holder.binding.imageView38.setVisibility(View.INVISIBLE);
                holder.binding.imageView41.setVisibility(View.VISIBLE);
                holder.binding.imageView40.setVisibility(View.INVISIBLE);
                holder.binding.imageView42.setVisibility(View.VISIBLE);

                String gelenAd = holder.binding.editTextText11.getText().toString();
                String gelenSoyad = holder.binding.editTextText12.getText().toString();
                String gelenEmail = holder.binding.editTextText13.getText().toString();
                String gelenTelefon = holder.binding.editTextText14.getText().toString();
                String gelenStatu = holder.binding.editTextText15.getText().toString();

                PersonelGuncelleRequest request = new PersonelGuncelleRequest();
                request.setName(gelenAd);
                request.setSurname(gelenSoyad);
                request.setEmail(gelenEmail);
                request.setPhone(gelenTelefon);
                request.setStatus(gelenStatu);


                holder.binding.imageView41.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Islemler personelGuncelle = new Islemler();
                        personelGuncelle.personelGuncelle(personelId, request, new Islemler.personelGuncelleCallback() {
                            @Override
                            public void onSuccess(String message) {

                                Toast.makeText(mContext, "Güncelleme başarıyla gerçekleşti.", Toast.LENGTH_LONG).show();
                                holder.binding.editTextText11.setVisibility(View.GONE);
                                holder.binding.editTextText12.setVisibility(View.GONE);
                                holder.binding.editTextText13.setVisibility(View.GONE);
                                holder.binding.editTextText14.setVisibility(View.GONE);
                                holder.binding.editTextText15.setVisibility(View.GONE);
                                personelIslemlerBinding.view6.setVisibility(View.GONE);

                                notifyItemChanged(holder.getAdapterPosition()); // recyclerview ogesini guncelle


                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(mContext, "Güncelleme başarısız!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });

                holder.binding.imageView42.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        personelIslemlerBinding.view6.setVisibility(View.GONE);
                        holder.binding.imageView38.setVisibility(View.VISIBLE);
                        holder.binding.imageView41.setVisibility(View.GONE);
                        holder.binding.imageView40.setVisibility(View.VISIBLE);
                        holder.binding.imageView42.setVisibility(View.GONE);
                        holder.binding.editTextText11.setVisibility(View.GONE);
                        holder.binding.editTextText12.setVisibility(View.GONE);
                        holder.binding.editTextText13.setVisibility(View.GONE);
                        holder.binding.editTextText14.setVisibility(View.GONE);
                        holder.binding.editTextText15.setVisibility(View.GONE);
                    }
                });


            }
        });
    }

    @Override
    public int getItemCount() {
        return personelListesi.size(); // gelen kullanici sayisini buraya verir ve bu sayiya hore createViewHolder icerisinde nesne olusturulur ve deger atamalari yapilir
    }


    public class cardNesneleriTutucu extends RecyclerView.ViewHolder {
        public PersonelcardtasarimiBinding binding;
        private TextView textView41, textView42, textView43, textView44, textView46;
        private ImageView imageView37, imageView38, imageView40;

        public cardNesneleriTutucu(PersonelcardtasarimiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.textView41 = binding.textView41;
            this.textView42 = binding.textView42;
            this.textView43 = binding.textView43;
            this.textView44 = binding.textView44;
            this.textView46 = binding.textView46;
            this.imageView37 = binding.imageView37;
            this.imageView38 = binding.imageView38;
            this.imageView40 = binding.imageView40;


        }
    }

    public void setData(List<Employee> yeniListe) {
        this.personelListesi = yeniListe;
        notifyDataSetChanged();
    }
}
