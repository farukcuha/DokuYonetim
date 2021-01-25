package com.example.dokuyonetim.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dokuyonetim.Adapters.UrunlerAdapter;
import com.example.dokuyonetim.R;
import com.example.dokuyonetim.Values.UrunlerValues;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

public class Urunler extends AppCompatActivity{
    private RecyclerView recyclerView;
    private UrunlerAdapter adapter;
    private Button urunEkle, urunSil;
    private AlertDialog.Builder builder;
    private static final int CAMERA_REQUEST_CODE = 101;
    private static final int GALLERY_REQUEST_CODE = 102;
    private ImageView resim;
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private String newImageUrl;
    private HashMap<String, Object> hashMap = new HashMap<>();
    private TextView bosyazi;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_urunler);

        recyclerView = findViewById(R.id.recyclerView);
        urunEkle = findViewById(R.id.urunekle);
        urunSil = findViewById(R.id.urunsil);
        bosyazi = findViewById(R.id.bosyazi);


        progressDialog = new ProgressDialog(Urunler.this);
        progressDialog.setMessage("Yükleniyor...");




        Query query = FirebaseFirestore.getInstance().collection("Ürünler").orderBy("ürünAdi", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<UrunlerValues> options = new FirestoreRecyclerOptions.Builder<UrunlerValues>()
                .setQuery(query, UrunlerValues.class)
                .build();

        adapter = new UrunlerAdapter(options, bosyazi);
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
                if(adapter.getDeleteProductNames().isEmpty()){
                    Toast.makeText(Urunler.this, "Ürün Seçiniz", Toast.LENGTH_SHORT).show();
                }
                else {
                    urunBilgiSil();
                }
            }
        });
    }

    private void urunBilgiSil() {
        adapter.deleteProduct(adapter.getDeleteProductNames(), Urunler.this);

    }

    private AlertDialog setUpAlertDialog(){
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.uruneklemedialog, null);
        resim = view.findViewById(R.id.resim);
        Button ekle = view.findViewById(R.id.ekle);

        EditText urunAdi = view.findViewById(R.id.ürünadi);
        EditText urunFiyati = view.findViewById(R.id.ürünfiyat);
        EditText urunAciklama = view.findViewById(R.id.urunAciklama);
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


        ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Urunler.this);
                CharSequence[] options = {"Kamera", "Galeri", "İptal"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if ("Kamera".equals(options[which])) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                if (checkSelfPermission(Manifest.permission.CAMERA) ==
                                        PackageManager.PERMISSION_DENIED ||
                                        checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                                PackageManager.PERMISSION_DENIED){
                                    String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                                    requestPermissions(permission, CAMERA_REQUEST_CODE);
                                }
                                else {
                                    openCamera();
                                }
                            }
                            else {
                                openCamera();
                            }


                        }
                        else if ("Galeri".equals(options[which])){
                            openGallery();
                        }
                        else if ("İptal".equals(options[which])){
                            dialog.dismiss();
                        }
                    }
                }).create().show();


            }
        });


        kargoSuresi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hashMap.put("ürünTahminiKargo", parent.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        satisTuru.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hashMap.put("ürünSatisTuru", parent.getSelectedItem().toString());
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
        builder = new AlertDialog.Builder(Urunler.this);
        builder.setTitle("Urun Ekle");
        builder.setView(view).setPositiveButton("Kaydet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                progressDialog.show();
                uploadInfos(urunAdi.getText().toString(), urunAciklama.getText().toString(), urunFiyati.getText().toString());


            }
        }).setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();


    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }
    private void openCamera(){
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        Uri image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        imageUri = image_uri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(intent, CAMERA_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CAMERA_REQUEST_CODE){
            Glide.with(Urunler.this).load(imageUri).centerCrop().into(resim);
        }
        else if(requestCode == GALLERY_REQUEST_CODE && data.getData() != null){
            Glide.with(Urunler.this).load(data.getData()).centerCrop().into(resim);
            imageUri = data.getData();
        }
        else {
            progressDialog.dismiss();
        }
    }
    private void uploadInfos(String urunAdi, String urunAciklama, String urunFiyati){
        StorageReference reference = FirebaseStorage.getInstance("gs://dokuapp-fcf7e.appspot.com").getReference().child("urunler/"+urunAdi);
        reference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    Glide.with(getApplicationContext()).load(imageUri).centerCrop().into(resim);
                    reference.getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if(task.isSuccessful()){
                                hashMap.put("ürünAciklama", urunAciklama);
                                hashMap.put("ürünAdi", urunAdi);
                                hashMap.put("ürünFiyati", urunFiyati);
                                hashMap.put("ürünResim", task.getResult().toString());
                                FirebaseFirestore.getInstance().collection("Ürünler").document(urunAdi)
                                        .set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            progressDialog.dismiss();

                                        }
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });

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