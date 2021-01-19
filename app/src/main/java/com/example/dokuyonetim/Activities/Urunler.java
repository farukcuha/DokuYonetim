package com.example.dokuyonetim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.example.dokuyonetim.Adapters.UrunlerAdapter;
import com.example.dokuyonetim.R;
import com.example.dokuyonetim.Values.UrunlerValues;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Urunler extends AppCompatActivity {
    private RecyclerView recyclerView;
    private UrunlerAdapter adapter;
    private Button urunEkle, urunSil;
    private AlertDialog.Builder builder;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urunler);

        recyclerView = findViewById(R.id.recyclerView);
        urunEkle = findViewById(R.id.urunekle);
        urunSil = findViewById(R.id.urunsil);

        
        Query query = FirebaseFirestore.getInstance().collection("Ürünler").orderBy("ürünAdi", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<UrunlerValues> options = new FirestoreRecyclerOptions.Builder<UrunlerValues>()
                .setQuery(query, UrunlerValues.class)
                .build();

        adapter = new UrunlerAdapter(options);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        urunEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpAlertDialog().show();
            }
        });

        urunSil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });



    }

    private AlertDialog setUpAlertDialog(){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.uruneklemedialog, null);
        ImageView resim = view.findViewById(R.id.resim);
        Button ekle = view.findViewById(R.id.ekle);
        EditText urunAdi = view.findViewById(R.id.ürünadi);
        EditText urunFiyati = view.findViewById(R.id.ürünfiyat);
        Spinner satisTuru = view.findViewById(R.id.satısturu);
        Spinner kargoSuresi = view.findViewById(R.id.kargosuresi);

        String[] kargoSecenekler = {"1 Gün", "2 Gün", "3 Gün", "4 Gün", "5 Gün", "6 Gün", "7 Gün"};
        String[] satisSecenekler = {"KG", "Adet", "Litre"};

        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(Urunler.this, android.R.layout.simple_spinner_dropdown_item, kargoSecenekler);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        kargoSuresi.setAdapter(adapter1);

        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(Urunler.this, android.R.layout.simple_spinner_dropdown_item, satisSecenekler);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        satisTuru.setAdapter(adapter2);



        builder = new AlertDialog.Builder(Urunler.this);
        builder.setTitle("Urun Ekle");
        builder.setView(view).setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();


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