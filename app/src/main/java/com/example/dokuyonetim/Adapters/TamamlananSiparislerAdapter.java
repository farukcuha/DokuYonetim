package com.example.dokuyonetim.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dokuyonetim.R;
import com.example.dokuyonetim.Values.GelenSiparislerValues;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class TamamlananSiparislerAdapter extends FirestoreRecyclerAdapter<GelenSiparislerValues, TamamlananSiparislerAdapter.Holder> {

    public TamamlananSiparislerAdapter(@NonNull FirestoreRecyclerOptions<GelenSiparislerValues> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull TamamlananSiparislerAdapter.Holder holder, int position, @NonNull GelenSiparislerValues model) {
        holder.siparisdurumu.setText(model.getSiparisDurumu());
        holder.fiyat.setText(model.getOdenenTutar());
        holder.siparistarihi.setText(model.getSiparisTarihi());
        holder.siparisno.setText(model.getSiparisNumarasi());
    }

    @NonNull
    @Override
    public TamamlananSiparislerAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gelensiparisitem, parent, false);
        return new Holder(view);
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView siparisno, siparistarihi, fiyat, siparisdurumu;

        public Holder(@NonNull View itemView) {
            super(itemView);
            siparisno = itemView.findViewById(R.id.siparisno);
            siparistarihi = itemView.findViewById(R.id.siparistarihi);
            fiyat = itemView.findViewById(R.id.toplamtutar);
            siparisdurumu = itemView.findViewById(R.id.siparisdurumu);
        }
    }
}
