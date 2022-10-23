package com.example.tesis.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
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

import java.util.HashMap;
import java.util.Map;

public class ViewActivityDashboard extends AppCompatActivity {

    ImageView imageView;
    TextView titulo, descripcion, textviewgeneral;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    double ratingg , promedio;

   private RatingBar ratingBar;

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
       ratingBar = findViewById(R.id.ratingBarServicio);


        if (id == null || id == "") {

            finish();
            startActivity(new Intent(ViewActivityDashboard.this, MainActivity.class));

        } else {
            getservvicio(id);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                String idusuario = mAuth.getCurrentUser().getUid();
                String idEstrella = id + idusuario;

                double rati = rating;

                Map<String,Object> map = new HashMap<>();
                map.put("idEstrella", idEstrella);
                map.put("idservicio", id);
                map.put("idusuario", idusuario);
                map.put("rating", rati);


                mFirestore.collection("estrellas").document(idEstrella).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(ViewActivityDashboard.this, "Calificaste el servcio con: "+ rating, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ViewActivityDashboard.this, "Error al calificar Servicio", Toast.LENGTH_SHORT).show();
                    }
                });

/*

                if(ratingg != rati){

                    Map<String,Object> promediorating = new HashMap<>();


                    Double promedionuevo = promedio - ratingg +rati;
                    map.put("promedio", promedionuevo);


                    if(promedionuevo != promedio){

                        mFirestore.collection("promedioestrellas")
                                .document("promediorating")
                                .set(promediorating).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {

                                    }
                                });


                    }



                }
                */

            }
   });


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
                String idusuario = mAuth.getCurrentUser().getUid();
                String idEstrella = id + idusuario;


                mFirestore.collection("estrellas").document(idEstrella).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot2) {

                      Double estrellas =documentSnapshot2.getDouble("rating");

                      float setratin;

                      if(estrellas == null){
                          setratin = 0;
                      }else{
                          setratin = estrellas.floatValue();
                      }

                        ratingBar.setRating(setratin);
                      ratingg = setratin;
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewActivityDashboard.this, "No se obtuvo el rating", Toast.LENGTH_SHORT).show();
                    }
                });


                mFirestore.collection("promedioestrellas").document("promediorating").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot3) {
                     //Double promedios = documentSnapshot3.getDouble("promedio");
                    // promedio = promedios;
                    }
                });






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
        //finish();
        //FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.navigation_dashboard);
        //FragmentTransaction transaction = manager.beginTransaction();

        //transaction.replace(R.id.navigation_dashboard,fragment );
       // transaction.addToBackStack(null);
        //transaction.commitNow();



        startActivity(new Intent(ViewActivityDashboard.this, fragment.getClass()));
        //onBackPressed();
        return false;
    }

}