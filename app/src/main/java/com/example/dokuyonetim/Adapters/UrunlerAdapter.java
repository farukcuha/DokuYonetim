package com.example.dokuyonetim.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.dokuyonetim.R;
import com.example.dokuyonetim.Values.UrunlerValues;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

public class UrunlerAdapter extends FirestoreRecyclerAdapter<UrunlerValues, UrunlerAdapter.Holder> {

    ArrayList<String> deleteProductNames = new ArrayList<>();
    private ProgressDialog pd;
    private TextView bosyazi;

    public UrunlerAdapter(@NonNull FirestoreRecyclerOptions<UrunlerValues> options, TextView bosyazi) {
        super(options);
        this.bosyazi = bosyazi;

    }



    public ArrayList<String> getDeleteProductNames() {
        return deleteProductNames;
    }

    public void deleteProduct(ArrayList<String> ad, Context context){
        pd = new ProgressDialog(context);
        pd.setMessage("Siliniyor...");
        pd.show();
        Log.d("a", String.valueOf(ad));
        for (String s : ad){
            FirebaseStorage.getInstance("gs://dokuapp-fcf7e.appspot.com").getReference().child("urunler/"+s)
                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        FirebaseFirestore.getInstance().collection("Ürünler").document(s).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    pd.dismiss();
                                    durumDinleme();

                                }
                            }
                        });
                    }
                }
            });
        }
    }




    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull UrunlerValues model) {
        Glide.with(holder.itemView.getContext()).load(model.getÜrünResim()).override(100, 100).centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.dialog.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.dialog.setVisibility(View.GONE);
                        return false;
                    }
                }).into(holder.resim);

        holder.urunFiyat.setText(model.getÜrünFiyati() + " ₺");
        holder.satisTuru.setText(model.getÜrünSatisTuru());
        holder.urunAd.setText(model.getÜrünAdi());

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    deleteProductNames.add(model.getÜrünAdi());
                }
                else {
                    deleteProductNames.remove(model.getÜrünAdi());
                }
            }
        });




    }

    private void durumDinleme(){
        if(getSnapshots().isEmpty()){
            bosyazi.setVisibility(View.VISIBLE);
        }
        else {
            bosyazi.setVisibility(View.GONE);
        }
    }
    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.urunler, parent, false);
    durumDinleme();
    return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ProgressBar dialog;
        private ImageView resim;
        private TextView urunAd, satisTuru, urunFiyat;
        private CheckBox checkBox;

        public Holder(@NonNull View itemView) {
            super(itemView);
            dialog = itemView.findViewById(R.id.progress_sepet);
            resim = itemView.findViewById(R.id.xml_spt_resim);
            urunAd = itemView.findViewById(R.id.xml_spt_ad);
            satisTuru = itemView.findViewById(R.id.xml_sepet_satısturu);
            urunFiyat = itemView.findViewById(R.id.xml_spt_urunfiyat);
            checkBox = itemView.findViewById(R.id.chechboxx);

        }
    }
}
