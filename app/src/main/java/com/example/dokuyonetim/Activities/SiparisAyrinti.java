package com.example.dokuyonetim.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dokuyonetim.Adapters.SiparisAyrintiAdapter;
import com.example.dokuyonetim.R;
import com.example.dokuyonetim.Values.SiparisAyrintiItems;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;

public class SiparisAyrinti extends AppCompatActivity{
    private TextView siparisAdSoyad, siparisNo, siparisTarihi, siparisTutar, siparisDurumu, kargoTakipNo;
    private TextView adresAd, adresAdSoyad, adres, adresIlIlce, adresTelNo;
    private RecyclerView recyclerView;
    private SiparisAyrintiAdapter adapter;
    private Bundle bundle;
    private ProgressDialog pd;
    private ImageView btn_kargo;
    private ImageView btn_kargono;
    private LinearLayout layout;

    private Spinner spinner;
    private EditText editKargoNo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelen_siparis_ayrinti);


        bundle = getIntent().getExtras();
        CharSequence[] options = {"Hazırlanıyor", "Kargoya Verildi", "Tamamlandı", "İptal Edildi"};
        String[] kargolar = {"MNG", "Yurtiçi", "Aras", "UPS", "Sürat", "PTT"};

        idPairs();
        setText();
        setUpRecyclerView();

        pd = new ProgressDialog(SiparisAyrinti.this);
        pd.setMessage("Yükleniyor...");

        FirebaseFirestore.getInstance().collection("Siparişler").document(bundle.getString("siparisno"))
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(value.get("siparisDurumu").equals("Kargoya Verildi")){
                            layout.setVisibility(View.VISIBLE);
                        }
                        else{
                            layout.setVisibility(View.GONE);
                        }
                    }
                });

        btn_kargo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SiparisAyrinti.this);
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

                AlertDialog.Builder builder = new AlertDialog.Builder(SiparisAyrinti.this);
                builder.setTitle("Kargo");
                View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.kargonotemdialog, null);
                HashMap<String, Object> hashMap = new HashMap<>();

                spinner = view.findViewById(R.id.spinner);
                editKargoNo = view.findViewById(R.id.kargono);

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SiparisAyrinti.this,
                        android.R.layout.simple_spinner_dropdown_item, kargolar);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String kargo = spinner.getSelectedItem().toString();
                        hashMap.put("kargoFirma", kargo);
                        Log.d("a", kargo);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });

                builder.setView(view);
                builder.setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pd.show();

                        hashMap.put("kargoTakipNo", editKargoNo.getText().toString());

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
                }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();

            }
        });
    }

    private void siparisDurumDbHelper(String durum){
        pd.show();
        HashMap<String, Object> hashMap = new HashMap<>();
        if(durum == "Tamamlandı"){
            hashMap.put("tamamlandimi", true);
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

        layout = findViewById(R.id.kargosatırı);

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






