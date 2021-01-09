package com.example.dokuyonetim.Activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.dokuyonetim.Adapters.GelenSiparislerAdapter;
import com.example.dokuyonetim.R;
import com.example.dokuyonetim.Values.GelenSiparislerValues;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class GelenSiparisler extends AppCompatActivity {
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private GelenSiparislerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelen_siparisler);

        Query query = firestore.collection("Siparişler").orderBy("odenenTutar", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<GelenSiparislerValues> options = new FirestoreRecyclerOptions.Builder<GelenSiparislerValues>()
                .setQuery(query, GelenSiparislerValues.class)
                .build();

        mAdapter = new GelenSiparislerAdapter(options);
        recyclerView = findViewById(R.id.aaaaaaaa);
        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mAdapter);


    }

    @Override
    protected void onStart() {
        super.onStart();
        mAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }
}


