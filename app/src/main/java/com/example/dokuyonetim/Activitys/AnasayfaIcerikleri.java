package com.example.dokuyonetim.Activitys;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.dokuyonetim.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.io.ByteArrayOutputStream;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AnasayfaIcerikleri extends AppCompatActivity {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private TextView anasayfaYazi;
    private Button resmidegistir, yaziyidegistir;
    private ImageView anaresim;
    YazıdegistirmeDialog yazıdegistirmeDialog;
    AlertDialog dialog;
    Uri image_uri;
    private static final int PERMISSION_CODE = 1000;
    private static final int PICK_IMAGE = 100;
    private static final int IMAGE_CAPTURE_CODE = 1001;

    private ProgressBar pd;

    FirebaseStorage mStorageReference = FirebaseStorage.getInstance("gs://dokuapp-fcf7e.appspot.com");
    StorageReference storageReference = mStorageReference.getReference().child("anasayfa.jpeg");

    @Override
    protected void onCreate(Bundle savedInstanceState) {;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa_icerikleri);
        anasayfaYazi = findViewById(R.id.anasayfayazi);
        resmidegistir = findViewById(R.id.resmidegistir);
        yaziyidegistir = findViewById(R.id.yazıyıdegistir);
        anaresim = findViewById(R.id.urunresim);
        pd = findViewById(R.id.horizontalprogress);

        currentItems();



        resmidegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog();
            }
        });


        yaziyidegistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yaziDegistirmeDialog();

            }
        });



    }


    private void dialog(){
        final CharSequence[] options = {"Kamera", "Galeri", "İptal"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AnasayfaIcerikleri.this);
        builder.setTitle("Resim Seç");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(options[which].equals("Kamera")){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                        if (checkSelfPermission(Manifest.permission.CAMERA) ==
                                PackageManager.PERMISSION_DENIED ||
                                checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) ==
                                        PackageManager.PERMISSION_DENIED){
                            String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                            requestPermissions(permission, PERMISSION_CODE);
                        }
                        else {
                            openCamera();
                        }
                    }
                    else {
                        openCamera();
                    }
                }
                else if(options[which].equals("Galeri")){
                    openGallery();
                }

                else if (options[which].equals("İptal")){
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, PICK_IMAGE);
    }

    private void yaziDegistirmeDialog(){
        yazıdegistirmeDialog = new YazıdegistirmeDialog();
        yazıdegistirmeDialog.show(getSupportFragmentManager(), "yazidialog");
        currentItems();
    }

    private void currentItems(){
        firestore.collection("Anasayfa Itemleri").document("İtemler")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {

                        anasayfaYazi.setText(String.valueOf(value.get("metin")));
                        Glide.with(getApplicationContext()).load(String.valueOf(value.get("resim"))).
                                placeholder(R.drawable.anasayfaitemborder).centerCrop().into(anaresim);
                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //this method is called, when user presses Allow or Deny from Permission Request Popup
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] ==
                        PackageManager.PERMISSION_GRANTED){
                    //permission from popup was granted
                    openCamera();
                }
                else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied...", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //set the image captured to our ImageView
            anaresim.setImageURI(image_uri);
            uploadImage();
        }
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){

            anaresim.setImageURI(data.getData());
            uploadImage();
        }
    }

    private void uploadImage(){
        anaresim.setDrawingCacheEnabled(true);
        anaresim.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) anaresim.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] data = baos.toByteArray();

        UploadTask task = storageReference.putBytes(data);

        task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pd.setProgress(0);
                    }
                }, 500);
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

            }
        });

        Task<Uri> uri = task.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });






    }


}























