package com.example.tesis;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesis.adapter.ServicioAdapterdasboard;
import com.example.tesis.model.Servicio;
import com.example.tesis.ui.dashboard.ViewActivityDashboard;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class serviciosdelusuario extends AppCompatActivity {


    RecyclerView mRecycler;
    ServicioAdapterdasboard mAdapter;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serviciosdelusuario);

        getSupportActionBar().setTitle("Servicios del usuario");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String id = getIntent().getStringExtra("idcreadordado");


        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = findViewById(R.id.RecyclerViewserviciosdelusuario);
        mRecycler.setLayoutManager(new LinearLayoutManager(serviciosdelusuario.this));
        Query query = mFirestore.collection("servicio");

        FirestoreRecyclerOptions<Servicio> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Servicio>().setQuery(query.orderBy("iddelcreador").startAt(id).endAt(id)
                        , Servicio.class).build();

        mAdapter = new ServicioAdapterdasboard(firestoreRecyclerOptions , serviciosdelusuario.this);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);




    }




    public boolean onSupportNavigateUp() {
        finish();
        //startActivity(new Intent(serviciosdelusuario.this, User.class));
        onBackPressed();
        return false;
    }



    @Override
    public void onStart() {
        super.onStart();

        mAdapter.startListening();

    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }


}
