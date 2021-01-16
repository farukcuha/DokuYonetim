package com.example.dokuyonetim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.dokuyonetim.Adapters.TamamlananSiparislerAdapter;
import com.example.dokuyonetim.R;
import com.example.dokuyonetim.Values.GelenSiparislerValues;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class TamamlananSiparisler extends AppCompatActivity {
    private RecyclerView recyclerView;
    private TamamlananSiparislerAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tamamlanan_siparisler);

        recyclerView = findViewById(R.id.tamamlanansiparisler);
        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query = FirebaseFirestore.getInstance().collection("Siparişler").whereEqualTo("tamamlandimi", true).orderBy("kargoTakipNo", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<GelenSiparislerValues> options = new FirestoreRecyclerOptions.Builder<GelenSiparislerValues>()
                .setQuery(query, GelenSiparislerValues.class)
                .build();


        adapter = new TamamlananSiparislerAdapter(options);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
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