package com.example.tesis;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class First_layout extends AppCompatActivity {



    EditText cajauser, cajapwd;
    Button btnsesion;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sesion);


        cajauser = (EditText) findViewById(R.id.editTextTextPersonName);
        cajapwd = (EditText) findViewById(R.id.editTextTextPersonpwd);
        btnsesion = (Button) findViewById(R.id.btnIniciarSesion);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        btnsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailuser = cajauser.getText().toString().trim();
                String pasword = cajapwd.getText().toString().trim();
                String nameuser= "johan";

                if(emailuser.isEmpty() && pasword.isEmpty()){

                    Toast.makeText(First_layout.this,"Complete los datos " +
                            cajauser.getText().toString(),Toast.LENGTH_SHORT).show();


                }else {
                    registerUser(nameuser,emailuser,pasword);

                }


            }

        });

        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_sesion, container, false);

    }

    private void registerUser(String nameuser, String emailuser, String pasword) {
        mAuth.createUserWithEmailAndPassword(emailuser,pasword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                String id = mAuth.getCurrentUser().getUid();
                Map<String,Object> map = new HashMap<>();
                map.put("id", id);
                map.put("nombre", nameuser);
                map.put("correo", emailuser);
                map.put("clave", pasword);

                mFirestore.collection("user").document(id).set(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(First_layout.this,"Usuario registrado " +
                                cajauser.getText().toString(),Toast.LENGTH_SHORT).show();

                        finish();

                        startActivity(new Intent(First_layout.this,MainActivity.class));


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(First_layout.this,"Error al guardar " +
                                cajauser.getText().toString(),Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(First_layout.this,"Error al registrar" +
                        cajauser.getText().toString(),Toast.LENGTH_SHORT).show();

            }
        });

    }



}


