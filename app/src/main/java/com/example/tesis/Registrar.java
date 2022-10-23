package com.example.tesis;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Registrar extends AppCompatActivity {




    EditText cajauser, cajapwd , cajaemail,cajacontacto;

    Button btnregistrar ,btnregresar;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;






    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_perfil);

        getSupportActionBar().setTitle("Registro");


        cajauser = (EditText) findViewById(R.id.edittextusuario);
        cajapwd = (EditText) findViewById(R.id.edittextclave);
        cajaemail = (EditText) findViewById(R.id.edittexEmail);
        cajacontacto = (EditText) findViewById(R.id.edittextcontacto);

        btnregistrar = (Button) findViewById(R.id.btnregistrarusuario);
        btnregresar = (Button) findViewById(R.id.btnregresar);




        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();









            btnregresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    finish();
                    startActivity(new Intent(Registrar.this, First_layout.class));

                }
            });







            btnregistrar.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    String nameuser = cajauser.getText().toString().trim();
                    String emailuser = cajaemail.getText().toString().trim();
                    String pasword = cajapwd.getText().toString().trim();
                    String contacto = cajacontacto.getText().toString().trim();

                    if (emailuser.isEmpty() && pasword.isEmpty() && nameuser.isEmpty() && contacto.isEmpty()) {

                        Toast.makeText(Registrar.this, "Complete los datos " +
                                cajauser.getText().toString(), Toast.LENGTH_SHORT).show();

                    } else {

                        registerUser(nameuser, emailuser, pasword, contacto);

                    }
                }
            });


        }



    public void registerUser(String nameuser, String emailuser, String pasword, String contacto) {
        mAuth.createUserWithEmailAndPassword(emailuser,pasword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                String id = mAuth.getCurrentUser().getUid();
                Map<String,Object> map = new HashMap<>();
                map.put("id", id);
                map.put("nombre", nameuser);
                map.put("correo", emailuser);
                map.put("clave", pasword);
                map.put("contacto", contacto);


                Map<String,Object> notis = new HashMap<>();

                notis.put("idnotificado", id);
                notis.put("titulo", "Bienvenido " + nameuser);
                notis.put("cuerpo","Te damos la bienvenida " + nameuser +", gracias por unirte a Clicking" );

                mFirestore.collection("user").document(id).set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                        mFirestore.collection("notificaciones").add(notis).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {


                            }
                        });


                        Toast.makeText(Registrar.this,"Usuario registrado " +
                                cajauser.getText().toString(),Toast.LENGTH_SHORT).show();

                        finish();
                        startActivity(new Intent(Registrar.this,MainActivity.class));

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(Registrar.this,"Error al guardar " +
                                cajauser.getText().toString(),Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Registrar.this,"Error al registrar" +
                        cajauser.getText().toString(),Toast.LENGTH_SHORT).show();

            }
        });
    }
}



