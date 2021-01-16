package com.example.dokuyonetim.Activities;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dokuyonetim.R;
import com.example.dokuyonetim.Values.KullanicilarValues;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class KullanicilarAdapter extends FirestoreRecyclerAdapter<KullanicilarValues, KullanicilarAdapter.Holder> {



    public KullanicilarAdapter(@NonNull FirestoreRecyclerOptions<KullanicilarValues> options) {
        super(options);
    }


    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kullaniciitem, parent, false);
        return new Holder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull Holder holder, int position, @NonNull KullanicilarValues model) {
        holder.adSoyad.setText(model.getAdSoyad());
        holder.email.setText(model.getEmail());
    }

    public class Holder extends RecyclerView.ViewHolder {
        private TextView adSoyad, email;

        public Holder(@NonNull View itemView) {
            super(itemView);
            adSoyad = itemView.findViewById(R.id.ad);
            email = itemView.findViewById(R.id.mail);
        }
    }
}
