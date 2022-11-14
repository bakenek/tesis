package com.example.tesis.ui.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ViewActivityDashboard extends AppCompatActivity {

    ImageView imageView;
    TextView titulo, descripcion, textviewgeneral,usuario , textviewpromedio;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    Button solicitar;

    Double ratingg;
    Boolean voto , solicito;

    String creadorid , serviname ;
    Double promedio , votante , solicitantes ;

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
        textviewpromedio = findViewById(R.id.textViewpromedio);
        titulo = findViewById(R.id.nombreserviciovista);
        descripcion = findViewById(R.id.descipcionserviciovista);
        usuario = findViewById(R.id.TextViewiraperfilusuario);

        imageView = findViewById(R.id.imageViewserviciovista);
       ratingBar = findViewById(R.id.ratingBarServicio);

       solicitar = findViewById(R.id.buttonsolicitar);


        if (id == null || id == "") {

            finish();
            startActivity(new Intent(ViewActivityDashboard.this, MainActivity.class));

        } else {
            getservvicio(id);


            solicitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    solicitar(id);
                }
            });


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


                if (solicito == true){}
                else if(solicito == false){}
                else if(solicito == null){solicito = false;}
                else{ solicito = false;}

                Map<String,Object> map = new HashMap<>();
                map.put("idEstrella", idEstrella);
                map.put("idservicio", id);
                map.put("idusuario", idusuario);
                map.put("rating", rati);
                map.put("voto", true);
                map.put("solicito", solicito);


                mFirestore.collection("estrellas").document(idEstrella).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                        mFirestore.collection("estrellas").document(idEstrella).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot documentSnapshot) {

                                mFirestore.collection("servicio").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {

                                                mFirestore.collection("servicio").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
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

                                                            Toast.makeText(ViewActivityDashboard.this,
                                                                    "Diste "+ rati + " estrellas", Toast.LENGTH_SHORT).show();
                                                        }

                                                        int votam = votant.intValue();
                                                        String plural;

                                                        if (votam == 1){
                                                            plural = " voto";
                                                        }
                                                        else{
                                                            plural = " votos";
                                                        }

                                                        Double promedioestrellas = promedi/votant;
                                                        textviewpromedio.setText("Promedio : "+ promedioestrellas
                                                                + " de " + votam + plural );

                                                        Map<String,Object> p= new HashMap<>();
                                                        p.put("promedio", promedi);
                                                        p.put("votantes", votant);
                                                        p.put("estrellas", promedioestrellas);


                                                        mFirestore.collection("servicio").document(id).update(p).addOnSuccessListener(new OnSuccessListener<Void>() {
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

                        Toast.makeText(ViewActivityDashboard.this, "Error al calificar Servicio", Toast.LENGTH_SHORT).show();
                    }
                });

            }
   });


        }


    }

    private void solicitar(String id) {

            String idusuario = mAuth.getCurrentUser().getUid();
            String idEstrella = id + idusuario;


                    mFirestore.collection("estrellas").document(idEstrella).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {

                            mFirestore.collection("servicio").document(id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {

                                            mFirestore.collection("servicio").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                @Override
                                                public void onSuccess(DocumentSnapshot documentSnapshot2) {

                                                    Double vot = documentSnapshot2.getDouble("solicitantes");
                                                    Double  solicitantes;

                                                    if(solicito == true){
                                                        solicitantes= vot;

                                                        Toast.makeText(ViewActivityDashboard.this, "El usuario ya fue notificado", Toast.LENGTH_SHORT).show();
                                                    }
                                                    else{
                                                        solicitantes= vot + 1;


                                                        Toast.makeText(ViewActivityDashboard.this,
                                                                  "Notificaremos al creador", Toast.LENGTH_SHORT).show();


                                                        mFirestore.collection("user").document(creadorid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                                String nombreCreador = documentSnapshot.getString("nombre");

                                                                Map<String,Object> notis = new HashMap<>();

                                                                notis.put("idnotificado", creadorid);
                                                                notis.put("titulo", nombreCreador +" muestra interes");
                                                                notis.put("cuerpo",nombreCreador + " solicito el servicio " + serviname   );


                                                                mFirestore.collection("notificaciones").add(notis).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                    @Override
                                                                    public void onSuccess(DocumentReference documentReference) {


                                                                    }
                                                                });



                                                            }
                                                        });



                                                    }

                                                    Map<String,Object> p= new HashMap<>();
                                                    p.put("solicitantes", solicitantes);



                                                    mFirestore.collection("servicio").document(id).update(p).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) { solicito = true;

                                                            Map<String,Object> e= new HashMap<>();
                                                            e.put("solicito",true);

                                                        mFirestore.collection("estrellas")
                                                                .document(idEstrella).update(e).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                    @Override
                                                                    public void onSuccess(Void unused) {

                                                                    }
                                                                });

                                                        }
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
                solicitantes = documentSnapshot.getDouble("solicitantes");
                String idusuario = mAuth.getCurrentUser().getUid();
                String idEstrella = id + idusuario;
                creadorid = idcreador;
                serviname = nombreServicio;

                Double votant = documentSnapshot.getDouble("votantes");
                Double prom = documentSnapshot.getDouble("estrellas");

                int votam = votant.intValue();
                String plural;

                if (votam == 1){
                    plural = " voto";
                }
                else{
                    plural = " votos";
                }

                textviewpromedio.setText("Promedio : "+ prom
                        + " de " + votam + plural );




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
                                        solicito = documentSnapshot2.getBoolean("solicito");

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
                                mep.put("solicito", false);


                                solicito = false;
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
                                + "Contacto: " + contactoCreador
                                + "\n"  + "Solicitado por " +solicitantes + " usuarios"
                                + "\n" + "Fecha de publicacion: " + FechaDeCreacionServicio );

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