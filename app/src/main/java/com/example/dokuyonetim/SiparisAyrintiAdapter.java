package com.example.dokuyonetim;

import android.graphics.drawable.Drawable;
import android.util.Log;
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
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class SiparisAyrintiAdapter extends FirestoreRecyclerAdapter<SiparisAyrintiItems, SiparisAyrintiAdapter.Holder> {

    public SiparisAyrintiAdapter(@NonNull FirestoreRecyclerOptions<SiparisAyrintiItems> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull SiparisAyrintiAdapter.Holder holder, int position, @NonNull SiparisAyrintiItems model) {
        holder.sepetUrunAdi.setText(model.getSepetUrunAdi());
        holder.sepetUrunAdet.setText(model.getSepetUrunAdet() + " Adet");

        Glide.with(holder.itemView.getContext()).load(model.getSepetUrunResim()).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                holder.pd.setVisibility(View.GONE);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                holder.pd.setVisibility(View.GONE);
                return false;
            }
        }).into(holder.sepetUrunResim);


        holder.sepetUrunToplamFiyati.setText(String.valueOf(model.getSepetUrunToplamFiyat()) + " ₺");
    }

    @NonNull
    @Override
    public SiparisAyrintiAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.siparisayrintiitems, parent, false);
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView sepetUrunAdi;
        TextView sepetUrunToplamFiyati;
        ImageView sepetUrunResim;
        TextView sepetUrunAdet;
        ProgressBar pd;


        public Holder(@NonNull View itemView) {
            super(itemView);
            sepetUrunAdi = itemView.findViewById(R.id.xml_spt_ad);
            sepetUrunToplamFiyati = itemView.findViewById(R.id.xml_spt_urunfiyat);
            sepetUrunResim = itemView.findViewById(R.id.xml_spt_resim);
            sepetUrunAdet = itemView.findViewById(R.id.xml_sepet_adet);
            pd = itemView.findViewById(R.id.progress_sepet);
        }
    }
}
