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
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.designs.databinding.FragmentPersonelIslemlerBinding;
import com.example.designs.databinding.PersonelcardtasarimiBinding;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.List;

public class RVAdapterPersonal extends RecyclerView.Adapter<RVAdapterPersonal.cardNesneleriTutucu> {

    private Context mContext;
    private List<Employee> personelListesi;
    private PersonelViewModel personelViewModel;
    private FragmentPersonelIslemlerBinding personelIslemlerBinding;



    public RVAdapterPersonal(Context mContext, List<Employee> personelListesi, PersonelViewModel personelViewModel) {
        this.mContext = mContext;
        this.personelListesi = personelListesi;
        this.personelViewModel = personelViewModel;

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

        // personel bilgilerinin tutuldugu editText ler
        EditText editText7 = holder.binding.editTextText7; //ad
        EditText editText8 = holder.binding.editTextText8; //soyad
        EditText editText9 = holder.binding.editTextText9; //email
        EditText editText10 = holder.binding.editTextText10; //phone
        EditText editText11 = holder.binding.editTextText11; // status

        // setData
        editText7.setText(employee.getName());
        editText8.setText(employee.getSurname());
        editText9.setText(employee.getEmail());
        editText10.setText(employee.getPhone());
        editText11.setText(employee.getStatus());
        holder.binding.imageView37.setImageResource(R.drawable.person);

        // reset image states
        holder.binding.imageView38.setImageResource(R.drawable.delete);
        holder.binding.imageView38.setTag("delete");

        holder.binding.imageView40.setImageResource(R.drawable.edit);
        holder.binding.imageView40.setTag("edit");

        // editTexlere veri girisini pasiflestirme
        setEditTextEnabled(false, editText7, editText8, editText9, editText10, editText11);

        // Edit icon click
        holder.binding.imageView40.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentTag = (String) holder.binding.imageView40.getTag();

                if ("edit".equals(currentTag)) {
                    // editleme gorseli aktifse
                    // delete; close, edit; check gorseline donusmelidir
                    holder.binding.imageView38.setImageResource(R.drawable.close);
                    holder.binding.imageView38.setTag("close");

                    holder.binding.imageView40.setImageResource(R.drawable.check);
                    holder.binding.imageView40.setTag("check");


                    // editText lere veri girisi aktiflestirilir
                    setEditTextEnabled(true, editText7, editText8, editText9, editText10, editText11);


                } else if ("check".equals(currentTag)) {
                    // check ikonuna tiklandiginda guncelleme isleminin gerceklesmesi gerekir
                    // oncelikle verileri request degiskenine set ettik
                    PersonelGuncelleRequest request = new PersonelGuncelleRequest();
                    request.setName(editText7.getText().toString());
                    request.setSurname(editText8.getText().toString());
                    request.setEmail(editText9.getText().toString());
                    request.setPhone(editText10.getText().toString());
                    request.setStatus(editText11.getText().toString());



                    personelViewModel.personelGuncelle(employee.getId(), request); // personel guncelle fonksiyonu

                    // reset UI : sayfanin guncel veriler ile gosterilmesi gerekir
                    holder.binding.imageView38.setImageResource(R.drawable.delete);
                    holder.binding.imageView38.setTag("delete");

                    holder.binding.imageView40.setImageResource(R.drawable.edit);
                    holder.binding.imageView40.setTag("edit");

                    setEditTextEnabled(false, editText7, editText8, editText9, editText10, editText11);

                }
            }
        });

        holder.binding.imageView38.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentTagUp = (String) holder.binding.imageView38.getTag();
                if ("delete".equals(currentTagUp)) {
                    // burada silme islemi yapilmaldir
                    new AlertDialog.Builder(mContext)
                            .setTitle("Kullanıcı Sil")
                            .setMessage("\" " + employee.getName() + " " + employee.getSurname() + "\"" + " adlı kullanıcıyı silmek istiyor musunuz ?")
                            .setPositiveButton("Evet", (dialog, which) -> {
                                personelViewModel.personelSil(employee.getId());

                            })
                            .setNegativeButton("Hayır", (dialog, which) -> {
                                dialog.dismiss();
                            })
                            .show();
                }
                else if ("close".equals(currentTagUp)){
                    setEditTextEnabled(false,editText7,editText8,editText9,editText10,editText11);

                    holder.binding.imageView38.setImageResource(R.drawable.delete);
                    holder.binding.imageView38.setTag("delete");

                    holder.binding.imageView40.setImageResource(R.drawable.edit);
                    holder.binding.imageView40.setTag("edit");


                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return personelListesi.size(); // gelen kullanici sayisini buraya verir ve bu sayiya hore createViewHolder icerisinde nesne olusturulur ve deger atamalari yapilir
    }


    public class cardNesneleriTutucu extends RecyclerView.ViewHolder {
        public PersonelcardtasarimiBinding binding;
        private EditText editText7, editText8, editText9, editText10, editText11;
        private ImageView imageView37, imageView38, imageView40;

        public cardNesneleriTutucu(PersonelcardtasarimiBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.editText7 = binding.editTextText7;
            this.editText8 = binding.editTextText8;
            this.editText9 = binding.editTextText9;
            this.editText10 = binding.editTextText10;
            this.editText11 = binding.editTextText11;
            this.imageView37 = binding.imageView37;
            this.imageView38 = binding.imageView38;
            this.imageView40 = binding.imageView40;


        }
    }

    public void setData(List<Employee> yeniListe) {
        this.personelListesi = yeniListe;
        notifyDataSetChanged();
    }

    private void setEditTextEnabled(boolean enabled, EditText... editText) { // ... anlami : degisken sayida parametre anlamina geliyor
        // yani istedigimiz sayida parametre verebiliriz
        for (EditText et : editText) {
            et.setEnabled(enabled);
            et.setFocusable(enabled);
            et.setFocusableInTouchMode(enabled);
            et.setCursorVisible(enabled);
            et.setBackground(enabled ? ContextCompat.getDrawable(mContext, R.drawable.bg4) : null);
            et.setPadding(20, 5, 20, 5);
        }
    }
}
