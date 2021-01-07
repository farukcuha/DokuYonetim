package com.example.dokuyonetim;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GelenSiparisAyrinti extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener{
    private Button bilgileriGuncelle;
    private TextView siparisAdSoyad, siparisNo, siparisTarihi, siparisTutar, siparisDurumu, kargoTakipNo;
    private TextView adresAd, adresAdSoyad, adres, adresIlIlce, adresTelNo;
    private RecyclerView recyclerView;

    private Button siparisAlertButton;
    private EditText kargoTakip, not;
    ProgressBar pd;
    private TextView kargoBilgisi;
    private LinearLayout linearLayout;

    String str_kargodurumu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelen_siparis_ayrinti);

        idPairs();

        bilgileriGuncelle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showPopup();

            }
        });

    }
    public void showPopup(View view){
        PopupMenu menu = new PopupMenu(this, view);
        menu.setOnMenuItemClickListener(this);
        menu.inflate(R.menu.siparisdurummenu);
        menu.show();

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()){
            case R.id.durum1:
                Log.d("a", "hazırlanıyor");
                return true;
            case R.id.durum3:
                Log.d("a", "durum3");
                return true;
            case R.id.durum4:
                Log.d("a", "durum4");
                return true;
            case R.id.kargo1:
                Log.d("a", "kargo1");
                return true;
            case R.id.kargo2:
                Log.d("a", "kargo2");
                return true;
            case R.id.kargo3:
                Log.d("a", "kargo3");
                return true;
            case R.id.kargo4:
                Log.d("a" ,"kargo4");
                return true;
            case R.id.kargo5:
                Log.d("a" ,"kargo5");
                return true;
            case R.id.kargo6:
                Log.d("a", "kargo6");
                return true;
        }
        str_kargodurumu = String.valueOf(item.getTitle());
        Log.d("a", str_kargodurumu);
        return true;
    }

    private void idPairs(){
        recyclerView = findViewById(R.id.recycler_view);
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

    private void showPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.siparisyonetdialog, null);
        siparisAlertButton = view.findViewById(R.id.siparisdurumu);
        kargoTakip = view.findViewById(R.id.kargotakip);
        not = view.findViewById(R.id.not);
        pd = view.findViewById(R.id.progres);
        kargoBilgisi = view.findViewById(R.id.kargobilgisi);
        linearLayout = view.findViewById(R.id.kargosatırı);


        builder.setView(R.layout.siparisyonetdialog).setTitle("Bilgileri Güncelle")
                .setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        pd.setVisibility(View.VISIBLE);

                    }
                }).
                setNegativeButton("İptal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();

        kargoBilgisi.setText(str_kargodurumu);


    }
    }






