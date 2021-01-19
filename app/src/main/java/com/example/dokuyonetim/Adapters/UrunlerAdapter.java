package com.example.dokuyonetim.Adapters;

import android.app.ProgressDialog;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

public class UrunlerAdapter extends FirestoreRecyclerAdapter<UrunlerValues, UrunlerAdapter.Holder> {

    public UrunlerAdapter(@NonNull FirestoreRecyclerOptions<UrunlerValues> options) {
        super(options);
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

    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.urunler, parent, false);
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder {
        private ProgressBar dialog;
        private ImageView resim;
        private TextView urunAd, satisTuru, urunFiyat;

        public Holder(@NonNull View itemView) {
            super(itemView);
            dialog = itemView.findViewById(R.id.progress_sepet);
            resim = itemView.findViewById(R.id.xml_spt_resim);
            urunAd = itemView.findViewById(R.id.xml_spt_ad);
            satisTuru = itemView.findViewById(R.id.xml_sepet_satısturu);
            urunFiyat = itemView.findViewById(R.id.xml_spt_urunfiyat);

        }
    }
}
