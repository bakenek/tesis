package com.example.tesis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class sobreti extends AppCompatActivity {


    ImageView usuariofoto;

    EditText  cajaestudio , cajahabilidades,cajaintereses;

    Button CrearServicio , regresar , eliminarfoto;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    StorageReference storageReference;
    String storage_path = "tesis/*";


    private static final int COD_SEL_STORAGE =200;
    private  static final  int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mas_sobre_ti);

        getSupportActionBar().setTitle("Editar perfil");
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        String id = mAuth.getCurrentUser().getUid();

        storageReference = FirebaseStorage.getInstance().getReference();

        eliminarfoto = (Button) findViewById(R.id.btneliminarsobremi);

        usuariofoto = findViewById(R.id.fotodeUsuario);


        cajaestudio = (EditText) findViewById(R.id.editTextEstudios);
        cajahabilidades = (EditText) findViewById(R.id.editTextHabilidades);
        cajaintereses  = (EditText) findViewById(R.id.editTextIntereses);



        CrearServicio = (Button) findViewById(R.id.btncontinuar);
        regresar= (Button) findViewById(R.id.btnomitir);
        progressBar = findViewById(R.id.progressBar5);

        CrearServicio.setText("Actualizar");
        getservvicio(id);



        usuariofoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPhoto();
            }
        });


        eliminarfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, Object> map = new HashMap<>();
                map.put("Photo","");
                mFirestore.collection("user").document(id).update(map);
                Toast.makeText(sobreti.this,"Foto eliminada", Toast.LENGTH_SHORT).show();

            }
        });


        CrearServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String estudios = cajaestudio.getText().toString().trim();
                String habilidades = cajahabilidades.getText().toString().trim();
                String intereses = cajaintereses.getText().toString().trim();



                Actualizarservicio(id , estudios , habilidades,intereses);






            }
        });

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(sobreti.this, MainActivity.class));
            }
        });


    }



    private void uploadPhoto() {
        Intent i = new Intent(Intent.ACTION_PICK);
        i.setType("image/*");

        startActivityForResult(i, COD_SEL_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == COD_SEL_IMAGE){
                image_url = data.getData();
                subirPhoto(image_url);


            }


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void subirPhoto(Uri image_url) {

        progressBar.setVisibility(View.VISIBLE);

        idd= mAuth.getCurrentUser().getUid();

        String rute_storage_photo = storage_path + "" + photo + "" + idd;
        StorageReference reference = storageReference.child(rute_storage_photo);
        reference.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                while (!uriTask.isSuccessful()){

                    if(uriTask.isSuccessful()){

                        uriTask.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String dowload_uri = uri.toString();
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("Photo", dowload_uri );
                                mFirestore.collection("user").document(idd).update(map);
                                Toast.makeText(sobreti.this, "Foto Actualizada", Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.INVISIBLE);
                            }
                        });


                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(sobreti.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });



    }









    private void Actualizarservicio(String id, String estudios, String habilidades, String intereses) {
        Map<String,Object> map = new HashMap<>();


        map.put("estudios",estudios);
        map.put("habilidades",habilidades);
        map.put("intereses",intereses);




        mFirestore.collection("user").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(sobreti.this, "Actualizado Exitosamente", Toast.LENGTH_SHORT).show();
                finish();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(sobreti.this, "Error al Actualizar", Toast.LENGTH_SHORT).show();
            }
        });



    }



    private void  getservvicio(String id){
        mFirestore.collection("user").document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String fotouser = documentSnapshot.getString("Photo");
                String estudios = documentSnapshot.getString("estudios");
                String habilidades= documentSnapshot.getString("habilidades");
                String intereses= documentSnapshot.getString("intereses");

                cajaestudio.setText(estudios);
                cajahabilidades.setText(habilidades);
                cajaintereses.setText(intereses);




                try {
                    if(!fotouser.equals("")){

                        Picasso.with(sobreti.this)
                                .load(fotouser)
                                .resize(150,150)
                                .into(usuariofoto);

                    }

                }catch (Exception e){
                    Log.v("Error", "e: "+ e);
                    Toast.makeText(sobreti.this,"Error", Toast.LENGTH_SHORT).show();



                }




            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(sobreti.this, "Error al Obtener los Datos", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        startActivity(new Intent(sobreti.this,MainActivity.class));
        //onBackPressed();
        return false;
    }
}

