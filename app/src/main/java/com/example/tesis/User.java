package com.example.tesis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tesis.ui.dashboard.ViewActivityDashboard;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class User extends AppCompatActivity {


    Button serviciodeuser;
    ImageView fotoudesuario;

    TextView nombre , correo, contacto,estudios,habilidades,intereses;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;



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


        fotoudesuario = findViewById(R.id.imageViewuser);

        obtenerdatos(id);

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


                    try {
                        if(!fotouser.equals("")){

                            Picasso.with(User.this)
                                    .load(fotouser)
                                    .resize(150,150)
                                    .into(fotoudesuario);

                        }

                    }catch (Exception e){
                        Log.v("Error", "e: "+ e);
                        Toast.makeText(User.this,"Error", Toast.LENGTH_SHORT).show();



                    }



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
