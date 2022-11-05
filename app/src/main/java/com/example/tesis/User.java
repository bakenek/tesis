package com.example.tesis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tesis.ui.dashboard.ViewActivityDashboard;
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

public class User extends AppCompatActivity {


    Button serviciodeuser;
    ImageView fotoudesuario;
    Double ratingg;
    Boolean voto;

    TextView nombre , correo, contacto,estudios,habilidades,intereses,textviewpromedio;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    RatingBar ratingBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.proveedor_perfil);


        getSupportActionBar().setTitle("Perfil de usuario");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        String id = getIntent().getStringExtra("idcreador");

        serviciodeuser = findViewById(R.id.serviciodeusuatio);

        nombre = findViewById(R.id.nombreuser);
        correo = findViewById(R.id.TextViewcorreo);
        contacto = findViewById(R.id.TextViewcontacto);
        estudios = findViewById(R.id.TextViewestudios);
        habilidades = findViewById(R.id.TextViewhabilidades);
        intereses = findViewById(R.id.TextViewintereses);
        ratingBar = findViewById(R.id.ratingBarUsuario);
        textviewpromedio = findViewById(R.id.textViewpromedioUsuario);


        fotoudesuario = findViewById(R.id.imageViewuser);



        if (id == null || id == "") {

            finish();
            startActivity(new Intent(User.this, MainActivity.class));

        } else {
        obtenerdatos(id);

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


                            mFirestore.collection("estrellas").document(idEstrella).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                @Override
                                public void onSuccess(DocumentSnapshot documentSnapshot) {

                                    mFirestore.collection("user").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                            if (task.isSuccessful()) {
                                                DocumentSnapshot document = task.getResult();
                                                if (document.exists()) {

                                                    mFirestore.collection("user").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                        @Override
                                                        public void onSuccess(DocumentSnapshot documentSnapshot2) {

                                                            Double prom = documentSnapshot2.getDouble("promedio");
                                                            Double vot = documentSnapshot2.getDouble("votantes");
                                                            Double promedi, votant;

                                                            if(voto == true){
                                                                promedi = prom - ratingg + rati;
                                                                votant= vot;
                                                                ratingg = rati;
                                                            }
                                                            else{
                                                                promedi = prom + rati;
                                                                votant = vot + 1;
                                                                ratingg = rati;
                                                                voto = true;

                                                                Toast.makeText(User.this,
                                                                        "Diste "+ rati + " estrellas", Toast.LENGTH_SHORT).show();
                                                            }


                                                            Double promedioestrellas = promedi/votant;
                                                            textviewpromedio.setText("Promedio : "+ promedioestrellas);

                                                            Map<String,Object> p= new HashMap<>();
                                                            p.put("promedio", promedi);
                                                            p.put("votantes", votant);
                                                            p.put("estrellas", promedioestrellas);


                                                            mFirestore.collection("user").document(id).update(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) { voto = true; }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                }
                                                            });



                                                        }
                                                    });

                                                }
                                            }else{

                                            }

                                        }
                                    });


                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(User.this, "Error al calificar Usuario", Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });


        }



        serviciodeuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(User.this
                        , serviciosdelusuario.class);
                i.putExtra("idcreadordado", id);
                startActivity(i);


            }
        });
    }

    private void obtenerdatos(String id) {
        mFirestore.collection("user").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String fotouser = documentSnapshot.getString("Photo");

                    String usuario = documentSnapshot.getString("nombre");
                    String correouser = documentSnapshot.getString("correo");
                    String contactouser = documentSnapshot.getString("contacto");
                    String estudiosuser= documentSnapshot.getString("estudios");
                    String habilidadesuser = documentSnapshot.getString("habilidades");
                    String interesesuser = documentSnapshot.getString("estudios");


                    nombre.setText(usuario);
                    correo.setText(correouser);
                    contacto.setText(contactouser);
                    estudios.setText(estudiosuser);
                    habilidades.setText(habilidadesuser);
                    intereses.setText(interesesuser);

                    String idusuario = mAuth.getCurrentUser().getUid();
                    String idEstrella = id + idusuario;


                    try {
                        if(!fotouser.equals("")){

                            Picasso.with(User.this)
                                    .load(fotouser)
                                    .resize(150,150)
                                    .into(fotoudesuario);

                        }

                    }catch (Exception e){
                        Log.v("Error", "e: "+ e);


                    }

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


                                    voto = false;
                                    ratingBar.setRating(0.0F);
                                    ratingg = Double.valueOf(i);

                                    mFirestore.collection("estrellas").document(idEstrella).set(mep).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                        }
                                    });



                                }
                            } else {
                                Toast.makeText(User.this, "No se obtuvo el rating", Toast.LENGTH_SHORT).show();

                            }
                        }



                    });







                }
            }
        });

    }


    public boolean onSupportNavigateUp() {
        finish();
        //startActivity(new Intent(User.this, ViewActivityDashboard.class));
        onBackPressed();
        return false;
    }



}
