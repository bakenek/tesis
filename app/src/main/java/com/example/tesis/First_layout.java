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
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class First_layout extends AppCompatActivity {

    EditText cajauser, cajapwd;

    Button btnsesion , btncuentas;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        setTheme(R.style.Theme_Tesis);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sesion);

        getSupportActionBar().setTitle("");

        cajauser = (EditText) findViewById(R.id.editTextTextPersonName);
        cajapwd = (EditText) findViewById(R.id.editTextTextPersonpwd);

        btnsesion = (Button) findViewById(R.id.btnIniciarSesion);
        btncuentas = (Button) findViewById(R.id.btncrear);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();





        btnsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailuser = cajauser.getText().toString().trim();
                String pasword = cajapwd.getText().toString().trim();


                if(emailuser.isEmpty() && pasword.isEmpty()){

                    Toast.makeText(First_layout.this,"Complete los datos " +
                            cajauser.getText().toString(),Toast.LENGTH_SHORT).show();


                }else {
                    loginUser(emailuser,pasword);

                }


            }

        });

        btncuentas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //finish();
                startActivity(new Intent(First_layout.this,Registrar.class));
            }
        });


        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_sesion, container, false);

    }

    private void loginUser( String emailuser, String pasword) {
      mAuth.signInWithEmailAndPassword(emailuser,pasword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
          @Override
          public void onComplete(@NonNull Task<AuthResult> task) {
              if(task.isSuccessful()){

                  finish();
                  startActivity(new Intent(First_layout.this, MainActivity.class ));

                  Toast.makeText(First_layout.this,"Bienvenido " +
                          cajauser.getText().toString(),Toast.LENGTH_SHORT).show();

              }else{

                  Toast.makeText(First_layout.this,"Error" +
                          cajauser.getText().toString(),Toast.LENGTH_SHORT).show();

              }

          }
      }).addOnFailureListener(new OnFailureListener() {
          @Override
          public void onFailure(@NonNull Exception e) {

              Toast.makeText(First_layout.this,"Error al iniciar sesion " +
                      cajauser.getText().toString(),Toast.LENGTH_SHORT).show();
          }
      });




    }

   @Override
   protected void onStart() {
        super.onStart();
       FirebaseUser user = mAuth.getCurrentUser();
       if(user != null){

       startActivity(new Intent(First_layout.this,MainActivity.class));
       finish();

  }
    }


}


