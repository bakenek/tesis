package com.example.tesis.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesis.AgregarServicio;
import com.example.tesis.MainActivity;
import com.example.tesis.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ViewActivityDashboard extends AppCompatActivity {

    ImageView imageView;
    TextView titulo, descripcion, textviewgeneral;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dashboard);

        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        String id = getIntent().getStringExtra("id_serviciodasboard");

        textviewgeneral = findViewById(R.id.TextViewcreadoFechaContacto);
        titulo = findViewById(R.id.nombreserviciovista);
        descripcion = findViewById(R.id.descipcionserviciovista);

        imageView = findViewById(R.id.imageViewserviciovista);


        if (id == null || id == "") {

            finish();
            startActivity(new Intent(ViewActivityDashboard.this, MainActivity.class));

        } else {
            getservvicio(id);

        }


    }



    private void  getservvicio(String id){

        String FechaDeCreacionServicio,idcreador;



        mFirestore.collection("servicio").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nombreServicio = documentSnapshot.getString("nombre");
                String descripcionServicio = documentSnapshot.getString("descripcion");
                String FechaDeCreacionServicio = documentSnapshot.getString("FechaDeCreacion");
                String idcreador = documentSnapshot.getString("iddelcreador");
                String fotoservicio = documentSnapshot.getString("Photo");


                try {
                    if(!fotoservicio.equals("")){

                        Picasso.with(ViewActivityDashboard.this)
                                .load(fotoservicio)
                                .resize(150,150)
                                .into(imageView);

                    }

                }catch (Exception e){
                    Log.v("Error", "e: "+ e);

                }













                titulo.setText(nombreServicio);
                descripcion.setText(descripcionServicio);



                mFirestore.collection("user").document(idcreador).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String nombreCreador = documentSnapshot.getString("nombre");
                        String contactoCreador = documentSnapshot.getString("contacto");
                        String correoCreador = documentSnapshot.getString("correo");







                        textviewgeneral.setText("Creado por: "+ nombreCreador + "\n"
                                + "Fecha de publicacion: " + FechaDeCreacionServicio + "\n"
                                + "Contacto: " + contactoCreador  + "\n" + "Correo: " + correoCreador
                        );

                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ViewActivityDashboard.this, "Error al Obtener los Datos", Toast.LENGTH_SHORT).show();
            }
        });


    }


    public boolean onSupportNavigateUp() {
        finish();
        startActivity(new Intent(ViewActivityDashboard.this, MainActivity.class));
        //onBackPressed();
        return false;
    }

}