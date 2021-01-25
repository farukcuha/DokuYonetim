package com.example.dokuyonetim.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import com.example.dokuyonetim.Adapters.GelenSiparislerAdapter;
import com.example.dokuyonetim.R;
import com.example.dokuyonetim.Values.GelenSiparislerValues;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.ServerTimestamp;

public class GelenSiparisler extends AppCompatActivity{
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private GelenSiparislerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private TextView bosyazi;
    private Spinner spinner;
    private Query queryWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelen_siparisler);
        bosyazi = findViewById(R.id.bosyazi2);

        queryWhere = firestore.collection("Siparişler").whereEqualTo("tamamlandimi", false)
                .orderBy("kargoTakipNo", Query.Direction.ASCENDING);
        setUpRecyclerView(queryWhere);
    }

    private void setUpRecyclerView(Query query){
        FirestoreRecyclerOptions<GelenSiparislerValues> options = new FirestoreRecyclerOptions.Builder<GelenSiparislerValues>()
                .setQuery(query, GelenSiparislerValues.class)
                .build();
        mAdapter = new GelenSiparislerAdapter(options, bosyazi);

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


