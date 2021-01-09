package com.example.dokuyonetim;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class GelenSiparisAyrinti extends AppCompatActivity{
    private Button bilgileriGuncelle;
    private TextView siparisAdSoyad, siparisNo, siparisTarihi, siparisTutar, siparisDurumu, kargoTakipNo;
    private TextView adresAd, adresAdSoyad, adres, adresIlIlce, adresTelNo;
    private RecyclerView recyclerView;

    private Button siparisAlertButton;
    private EditText kargoTakip, not;
    ProgressBar pd;
    private TextView kargoBilgisi;
    private LinearLayout linearLayout;
    private SiparisAyrintiAdapter adapter;

    String str_kargodurumu;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelen_siparis_ayrinti);

        bundle = getIntent().getExtras();

        idPairs();
        setText();
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("Siparişler").document(bundle.getString("siparisno"))
                .collection("Ürünler").orderBy("sepetUrunAdet", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<SiparisAyrintiItems> options = new FirestoreRecyclerOptions.Builder<SiparisAyrintiItems>()
                .setQuery(query, SiparisAyrintiItems.class)
                .build();
        adapter = new SiparisAyrintiAdapter(options);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }


    private void idPairs(){
        recyclerView = findViewById(R.id.recyclerView3);
        //bilgileriGuncelle = findViewById(R.id.button);

        siparisAdSoyad = findViewById(R.id.siparisadsoyad);
        siparisNo = findViewById(R.id.siparisno);
        siparisTarihi = findViewById(R.id.siparistarihi);
        siparisTutar = findViewById(R.id.toplamtutar);
        siparisDurumu = findViewById(R.id.siparisdurumu);
        kargoTakipNo = findViewById(R.id.kargotakipno);

        adresAd = findViewById(R.id.adresadii);
        adresAdSoyad = findViewById(R.id.adsoyad);
        adres = findViewById(R.id.adres);
        adresIlIlce = findViewById(R.id.ililce);
        adresTelNo = findViewById(R.id.telefonno);

    }

    private void setText(){


        siparisAdSoyad.setText(bundle.getString("Ad Soyad"));
        siparisNo.setText(bundle.getString("siparisno"));
        siparisTarihi.setText(bundle.getString("siparistarihi"));
        siparisTutar.setText(bundle.getString("fiyat"));
        siparisDurumu.setText(bundle.getString("siparisdurumu"));

        adresAd.setText(bundle.getString("Adres Başlığı"));
        adresAdSoyad.setText(bundle.getString("Ad Soyad"));
        adres.setText(bundle.getString("Adres"));
        adresIlIlce.setText(bundle.getString("İlİlçe"));
        adresTelNo.setText(bundle.getString("Telefon no"));



    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}






