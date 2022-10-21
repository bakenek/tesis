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

public class ActualizarUsuario extends AppCompatActivity {

    EditText cajauser, cajapwd , cajaemail,cajacontacto;

    Button CrearServicio , regresar;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actualizar_usuario);

        getSupportActionBar().setTitle("Actualizar datos");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        String id = mAuth.getCurrentUser().getUid();

        cajauser = (EditText) findViewById(R.id.edittextusuarioActualizar);
        cajapwd = (EditText) findViewById(R.id.edittextclaveActualizar);
        cajaemail = (EditText) findViewById(R.id.edittexEmailActualizar);
        cajacontacto = (EditText) findViewById(R.id.edittextcontactoActualizar);

        CrearServicio = (Button) findViewById(R.id.btnActualizarusuario);
        regresar= (Button) findViewById(R.id.btnActualizarregresar);




            CrearServicio.setText("Actualizar");
            getservvicio(id);


            CrearServicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nombreServicio = cajauser.getText().toString().trim();
                    String email = cajaemail.getText().toString().trim();
                    String clave = cajapwd.getText().toString().trim();
                    String contacto = cajacontacto.getText().toString().trim();





                    if(nombreServicio.isEmpty() && email.isEmpty()
                            && clave.isEmpty() && contacto.isEmpty()){

                        Toast.makeText(ActualizarUsuario.this, "Error al cargar datos", Toast.LENGTH_SHORT).show();

                    }else{

                        Actualizarservicio(nombreServicio,email,clave,contacto, id);


                    }



                }
            });

            regresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    startActivity(new Intent(ActualizarUsuario.this, MainActivity.class));
                }
            });


    }

    private void Actualizarservicio(String nombreServicio, String email,String clave, String contacto, String id) {
        Map<String,Object> map = new HashMap<>();

        map.put("nombre", nombreServicio);
        map.put("correo",email);
        map.put("clave",clave);
        map.put("contacto",contacto);

        mFirestore.collection("user").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(ActualizarUsuario.this, "Actualizado Exitosamente", Toast.LENGTH_SHORT).show();
                finish();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActualizarUsuario.this, "Error al Actualizar", Toast.LENGTH_SHORT).show();
            }
        });



    }



    private void  getservvicio(String id){
        mFirestore.collection("user").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String nombreServicio = documentSnapshot.getString("nombre");
                String email = documentSnapshot.getString("correo");
                String clave = documentSnapshot.getString("clave");
                String contacto = documentSnapshot.getString("contacto");

                cajauser.setText(nombreServicio);
                cajapwd.setText(clave);
                cajaemail.setText(email);
                cajacontacto.setText(contacto);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ActualizarUsuario.this, "Error al Obtener los Datos", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        startActivity(new Intent(ActualizarUsuario.this,MainActivity.class));
        //onBackPressed();
        return false;
    }
}

