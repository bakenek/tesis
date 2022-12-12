package com.example.tesis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
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

                    }
                    else if (emailuser.isEmpty() || pasword.isEmpty() || nameuser.isEmpty() || contacto.isEmpty()) {

                        Toast.makeText(Registrar.this, "Complete los datos " +
                                cajauser.getText().toString(), Toast.LENGTH_SHORT).show();

                    } else {


                        if(pasword.length() >= 6) {

                            if(validaremail(emailuser)) {
                                registerUser(nameuser, emailuser, pasword, contacto);

                            }else{
                                Toast.makeText(Registrar.this, "El correo no es valido"
                                        , Toast.LENGTH_SHORT).show();

                            }
                        }else{Toast.makeText(Registrar.this, "la clave no puede ser menor de 6 letras "
                                , Toast.LENGTH_SHORT).show();}

                    }
                }
            });


        }

    private boolean validaremail(String emailuser) {
        if(!emailuser.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailuser).matches()){

            return true;

        }else{
            return false;

        }


    }


    public void registerUser(String nameuser, String emailuser, String pasword, String contacto) {
        mAuth.createUserWithEmailAndPassword(emailuser,pasword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                Double i = Double.valueOf(0);
                String id = mAuth.getCurrentUser().getUid();
                Map<String,Object> map = new HashMap<>();
                map.put("id", id);
                map.put("nombre", nameuser);
                map.put("correo", emailuser);
                map.put("clave", pasword);
                map.put("contacto", contacto);
                map.put("Photo","");
                map.put("estudios","");
                map.put("habilidades","");
                map.put("intereses","");
                map.put("promedio", i);
                map.put("votantes", i);
                map.put("estrellas", i);


                String date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date());
                Map<String,Object> notis = new HashMap<>();

                notis.put("idnotificado", id);
                notis.put("titulo", "Bienvenido " + nameuser);
                notis.put("cuerpo","Te damos la bienvenida " + nameuser +", gracias por unirte a Clicking" );
                notis.put("fecha", date);

                mFirestore.collection("user").document(id).set(map)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {


                        mFirestore.collection("notificaciones").add(notis).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {


                            }
                        });


                        Toast.makeText(Registrar.this,"Usuario " +
                                cajauser.getText().toString() + "  registrado exitosamente",Toast.LENGTH_SHORT).show();

                        finish();
                        startActivity(new Intent(Registrar.this,sobreti.class));

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



