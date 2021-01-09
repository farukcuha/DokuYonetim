package com.example.dokuyonetim.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dokuyonetim.Adapters.SiparisAyrintiAdapter;
import com.example.dokuyonetim.R;
import com.example.dokuyonetim.Values.SiparisAyrintiItems;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class GelenSiparisAyrinti extends AppCompatActivity{
    private TextView siparisAdSoyad, siparisNo, siparisTarihi, siparisTutar, siparisDurumu, kargoTakipNo;
    private TextView adresAd, adresAdSoyad, adres, adresIlIlce, adresTelNo;
    private RecyclerView recyclerView;
    private SiparisAyrintiAdapter adapter;
    private Bundle bundle;
    private ProgressDialog pd;
    private ImageView btn_kargo;
    private ImageView btn_kargono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelen_siparis_ayrinti);

        bundle = getIntent().getExtras();
        CharSequence[] options = {"Hazırlanıyor", "Kargoya Verildi", "Tamamlandı", "İptal Edildi"};

        idPairs();
        setText();
        setUpRecyclerView();

        btn_kargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd = new ProgressDialog(GelenSiparisAyrinti.this);
                pd.setMessage("Yükleniyor...");
                AlertDialog.Builder builder = new AlertDialog.Builder(GelenSiparisAyrinti.this);
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(options[which].equals("Hazırlanıyor")){
                            siparisDurumDbHelper("Hazırlanıyor");
                            dialog.dismiss();
                        }
                        else if(options[which].equals("Kargoya Verildi")){
                            siparisDurumDbHelper("Kargoya Verildi");
                            dialog.dismiss();

                        }
                        else if(options[which].equals("Tamamlandı")){
                            siparisDurumDbHelper("Tamamlandı");
                            dialog.dismiss();


                        }
                        else if(options[which].equals("İptal Edildi")){
                            siparisDurumDbHelper("İptal Edildi");
                            dialog.dismiss();


                        }


                    }
                }).show();

            }
        });

        btn_kargono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void siparisDurumDbHelper(String durum){
        pd.show();
        HashMap<String, Object> hashMap = new HashMap<>();
        if(durum == "Tamamlandı"){
            hashMap.put("tamamlandimi?", true);
        }
        else {
            hashMap.put("siparisDurumu", durum);
            siparisDurumu.setText(durum);
            siparisDurumu.setTextColor(Color.RED);


            FirebaseFirestore.getInstance().collection("Siparişler").document(bundle.getString("siparisno"))
                    .set(hashMap, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        pd.dismiss();
                    }

                }
            });
        }

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

        btn_kargo = findViewById(R.id.btn1);
        btn_kargono = findViewById(R.id.btn2);

    }

    private void setText(){


        siparisAdSoyad.setText("Ad/Soyad: " + bundle.getString("Ad Soyad"));
        siparisNo.setText("Sipariş No: "+bundle.getString("siparisno"));
        siparisTarihi.setText("Tarih: "+bundle.getString("siparistarihi"));
        siparisTutar.setText("Toplam Tutar: "+bundle.getString("fiyat"));
        siparisDurumu.setText("Sipariş Durumu: "+bundle.getString("siparisdurumu"));

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






