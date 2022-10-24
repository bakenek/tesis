package com.example.tesis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AgregarServicio extends AppCompatActivity {

    EditText nombre, descripcion;

    ImageView fotoservicio;

    Button CrearServicio , eliminarfoto,eliminarservicio;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;



    StorageReference storageReference;
    String storage_path = "tesis/*";


    private static final int COD_SEL_STORAGE =200;
    private  static final  int COD_SEL_IMAGE = 300;

    private Uri image_url;
    String photo = "photo";
    String idd ,idservicio;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_servicio);

        getSupportActionBar().setTitle("  Nuevo Servicio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        storageReference = FirebaseStorage.getInstance().getReference();

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        String id = getIntent().getStringExtra("id_servicio");
        idservicio = id;

        fotoservicio = findViewById(R.id.imageViewserviciovista);

        nombre = (EditText) findViewById(R.id.edittextNombreServicio);
        descripcion = (EditText) findViewById(R.id.edittextDescripcionServicio);

        CrearServicio = (Button) findViewById(R.id.btnCrearServicio);

        eliminarfoto =(Button) findViewById(R.id.btneliminarfotoseervi);
        eliminarservicio =(Button) findViewById(R.id.btnborrarservicio);


        if(id == null || id == ""){

            eliminarservicio.setVisibility(View.GONE);
            eliminarfoto.setVisibility(View.GONE);
            fotoservicio.setVisibility(View.GONE);
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


            eliminarfoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HashMap<String, Object>map = new HashMap<>();
                    map.put("Photo","");
                    mFirestore.collection("servicio").document(idservicio).update(map);

                    Toast.makeText(AgregarServicio.this,"Foto eliminada", Toast.LENGTH_SHORT).show();
                }
            });



            eliminarservicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteServicio(id);

                }
            });



            fotoservicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    uploadPhoto();
                }
            });




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

    private void deleteServicio(String id) {

        mFirestore.collection("servicio").document(id).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.navigation_actividad);
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.navigation_actividad,fragment );
                transaction.addToBackStack(null);
                transaction.commit();

                Toast.makeText(AgregarServicio.this, "Servicio eliminado exitosamente", Toast.LENGTH_SHORT).show();


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarServicio.this, "Error al eliminar Servicio", Toast.LENGTH_SHORT).show();
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

        idd= mAuth.getCurrentUser().getUid();

        String rute_storage_photo = storage_path + "" + photo + "" + idd + ""+ idservicio;
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
                                mFirestore.collection("servicio").document(idservicio).update(map);
                                Toast.makeText(AgregarServicio.this, "Foto Actualizada", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AgregarServicio.this, "Error al cargar foto", Toast.LENGTH_SHORT).show();
            }
        });



    }





    private void Actualizarservicio(String nombreServicio, String descripcionServicio, String id) {

        Map<String,Object> map = new HashMap<>();

        map.put("nombre", nombreServicio);
        map.put("descripcion",descripcionServicio);




        mFirestore.collection("servicio").document(id).update(map).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(AgregarServicio.this, "Actualizado Exitosamente", Toast.LENGTH_SHORT).show();
               // finish();

                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.navigation_actividad);
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.navigation_actividad,fragment );
                transaction.addToBackStack(null);
                transaction.commit();


                //startActivity(new Intent(AgregarServicio.this,MainActivity.class));



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

        Map<String,Object> notis = new HashMap<>();

        notis.put("idnotificado", id);
        notis.put("titulo", "Creaste un servicio");
        notis.put("cuerpo","El Servicio: " + nombreServicio + ", fue creado con exito." );








        mFirestore.collection("servicio").add(map).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
            @Override
            public void onSuccess(DocumentReference documentReference) {


                mFirestore.collection("notificaciones").add(notis).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {


                    }
                });

                Toast.makeText(AgregarServicio.this, "Creado exitosamente", Toast.LENGTH_SHORT).show();
                //finish();
                FragmentManager manager = getSupportFragmentManager();
                Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.navigation_actividad);
                FragmentTransaction transaction = manager.beginTransaction();

                transaction.replace(R.id.navigation_actividad,fragment );
                transaction.addToBackStack(null);
                transaction.commit();


                //startActivity(new Intent(AgregarServicio.this,MainActivity.class));

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
                String fotoServi = documentSnapshot.getString("Photo");

                nombre.setText(nombreServicio);
                descripcion.setText(descripcionServicio);

                try {
                    if(!fotoServi.equals("")){

                        Picasso.with(AgregarServicio.this)
                                .load(fotoServi)
                                .resize(150,150)
                                .into(fotoservicio);

                    }

                }catch (Exception e){
                    Log.v("Error", "e: "+ e);


                }





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
       //finish();
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.navigation_actividad);
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.replace(R.id.navigation_actividad,fragment );
        transaction.addToBackStack(null);
        transaction.commitNow();

       // startActivity(new Intent(AgregarServicio.this,fragment.getClass()));

       // super.onBackPressed();
        return false;
    }
}
