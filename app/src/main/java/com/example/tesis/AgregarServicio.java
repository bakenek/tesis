package com.example.tesis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AgregarServicio extends AppCompatActivity {

    EditText nombre, descripcion;

    Button CrearServicio;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_servicio);

        getSupportActionBar().setTitle("  Nuevo Servicio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        String id = getIntent().getStringExtra("id_servicio");

        nombre = (EditText) findViewById(R.id.edittextNombreServicio);
        descripcion = (EditText) findViewById(R.id.edittextDescripcionServicio);

        CrearServicio = (Button) findViewById(R.id.btnCrearServicio);


        if(id == null || id == ""){

            CrearServicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    String nombreServicio = nombre.getText().toString().trim();
                    String descripcionServicio = descripcion.getText().toString().trim();

                    if(nombreServicio.isEmpty() && descripcionServicio.isEmpty()){

                        Toast.makeText(AgregarServicio.this, "Ingresa los Datos", Toast.LENGTH_SHORT).show();

                    }else{

                        agregarservicio(nombreServicio,descripcionServicio);


                    }


                }
            });




        }else{

            CrearServicio.setText("Actualizar Servicio");
            getservvicio(id);


            CrearServicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nombreServicio = nombre.getText().toString().trim();
                    String descripcionServicio = descripcion.getText().toString().trim();

                    if(nombreServicio.isEmpty() && descripcionServicio.isEmpty()){

                        Toast.makeText(AgregarServicio.this, "Ingresa los Datos", Toast.LENGTH_SHORT).show();

                    }else{

                        Actualizarservicio(nombreServicio,descripcionServicio, id);


                    }



                }
            });


        }









    }

    private void Actualizarservicio(String nombreServicio, String descripcionServicio, String id) {
        Map<String,Object> map = new HashMap<>();

        map.put("nombre", nombreServicio);
        map.put("descripcion",descripcionServicio);

        mFirestore.collection("servicio").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AgregarServicio.this, "Actualizado Exitosamente", Toast.LENGTH_SHORT).show();
                finish();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarServicio.this, "Error al Actualizar", Toast.LENGTH_SHORT).show();
            }
        });



    }

    private void agregarservicio(String nombreServicio, String descripcionServicio) {

        String id = mAuth.getCurrentUser().getUid();
        String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());

        Map<String,Object> map = new HashMap<>();

        map.put("iddelcreador", id);
        map.put("nombre", nombreServicio);
        map.put("descripcion",descripcionServicio);
        map.put("FechaDeCreacion",date);





        mFirestore.collection("servicio").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {


                Toast.makeText(AgregarServicio.this, "Creado exitosamente", Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(AgregarServicio.this,MainActivity.class));

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarServicio.this, "Error al ingresar", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void  getservvicio(String id){
        mFirestore.collection("servicio").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nombreServicio = documentSnapshot.getString("nombre");
                String descripcionServicio = documentSnapshot.getString("descripcion");

                nombre.setText(nombreServicio);
                descripcion.setText(descripcionServicio);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarServicio.this, "Error al Obtener los Datos", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        startActivity(new Intent(AgregarServicio.this,MainActivity.class));
        //onBackPressed();
        return false;
    }
}
