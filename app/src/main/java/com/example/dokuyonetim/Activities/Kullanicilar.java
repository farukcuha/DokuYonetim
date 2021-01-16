package com.example.dokuyonetim.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.example.dokuyonetim.R;
import com.example.dokuyonetim.Values.KullanicilarValues;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Kullanicilar extends AppCompatActivity {
    private RecyclerView recyclerView;
    private KullanicilarAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kullanicilar);
        recyclerView = findViewById(R.id.recycler_view);

        recyclerView = findViewById(R.id.recycler_view);
        Query query = FirebaseFirestore.getInstance().collection("Kullanıcılar")
                .orderBy("email", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<KullanicilarValues> options = new FirestoreRecyclerOptions.Builder<KullanicilarValues>()
                .setQuery(query, KullanicilarValues.class)
                .build();

        adapter = new KullanicilarAdapter(options);
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));



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

