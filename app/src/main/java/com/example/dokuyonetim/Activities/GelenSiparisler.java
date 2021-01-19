package com.example.dokuyonetim.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.ServerTimestamp;

public class GelenSiparisler extends AppCompatActivity{
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private GelenSiparislerAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private RecyclerView recyclerView;
    private TextView text;
    private Spinner spinner;
    private Query queryWhere;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gelen_siparisler);
        text = findViewById(R.id.textvieww);
        //spinner = findViewById(R.id.gelenspinner);

        queryWhere = firestore.collection("Siparişler").whereEqualTo("tamamlandimi", false)
                .orderBy("odenenTutar", Query.Direction.ASCENDING);
        setUpRecyclerView(queryWhere);

        /*String[] olcutler = {"Fiyat +", "Fiyat -", "Tarih +", "Tarih -"};
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, olcutler);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (parent.getSelectedItem().toString()){
                    case "Fiyat+":
                        queryAscDsc = queryWhere.orderBy("odenenTutar", Query.Direction.ASCENDING);
                        setUpRecyclerView(queryAscDsc);
                        break;
                    case "Fiyat -":
                        queryAscDsc = queryWhere.orderBy("odenenTutar", Query.Direction.DESCENDING);
                        setUpRecyclerView(queryAscDsc);

                        break;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

    }

    private void setUpRecyclerView(Query query){
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


