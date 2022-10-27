package com.example.tesis.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesis.MainActivity;
import com.example.tesis.R;
import com.example.tesis.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ViewActivityDashboard extends AppCompatActivity {

    ImageView imageView;
    TextView titulo, descripcion, textviewgeneral,usuario;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    Double ratingg;
    Boolean voto;

    String creadorid;

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
        usuario = findViewById(R.id.TextViewiraperfilusuario);

        imageView = findViewById(R.id.imageViewserviciovista);
       ratingBar = findViewById(R.id.ratingBarServicio);


        if (id == null || id == "") {

            finish();
            startActivity(new Intent(ViewActivityDashboard.this, MainActivity.class));

        } else {
            getservvicio(id);


            usuario.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(ViewActivityDashboard.this
                            , User.class);
                    i.putExtra("idcreador", creadorid);
                    startActivity(i);


                }
            });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {

                String idusuario = mAuth.getCurrentUser().getUid();
                String idEstrella = id + idusuario;

                Double rati = Double.valueOf(rating);


                Map<String,Object> map = new HashMap<>();
                map.put("idEstrella", idEstrella);
                map.put("idservicio", id);
                map.put("idusuario", idusuario);
                map.put("rating", rati);
                map.put("voto", true);



                mFirestore.collection("estrellas").document(idEstrella).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        //Toast.makeText(ViewActivityDashboard.this, "Calificaste el servcio con: "+ rating, Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(ViewActivityDashboard.this, "Error al calificar Servicio", Toast.LENGTH_SHORT).show();
                    }
                });

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
                creadorid = idcreador;



                mFirestore.collection("estrellas").document(idEstrella).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                mFirestore.collection("estrellas").document(idEstrella).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot2) {

                                        Double estrellas = Double.valueOf(0);

                                        estrellas =documentSnapshot2.getDouble("rating");
                                        voto = documentSnapshot2.getBoolean("voto");

                                        float setratin = estrellas.floatValue();
                                        Float object = setratin;


                                        if (object != null) {
                                            setratin = estrellas.floatValue();
                                            ratingBar.setRating(setratin);
                                            ratingg = Double.valueOf(setratin);
                                        } else {

                                            ratingBar.setRating(0.0F);
                                            ratingg = 0.0;
                                        }




                                    }
                                });
                            } else {
                                Double i = Double.valueOf(0);
                                Map<String,Object> mep= new HashMap<>();
                                mep.put("idEstrella", idEstrella);
                                mep.put("idservicio", id);
                                mep.put("idusuario", idusuario);
                                mep.put("rating", i);
                                mep.put("voto", false);

                                mFirestore.collection("estrellas").document(idEstrella).set(mep).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                });



                            }
                        } else {
                            Toast.makeText(ViewActivityDashboard.this, "No se obtuvo el rating", Toast.LENGTH_SHORT).show();

                        }
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

                        usuario.setText(nombreCreador);
                        textviewgeneral.setText(  "Correo: " +  correoCreador + "\n"
                                + "Contacto: " + contactoCreador  + "\n" + "Fecha de publicacion: " + FechaDeCreacionServicio
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