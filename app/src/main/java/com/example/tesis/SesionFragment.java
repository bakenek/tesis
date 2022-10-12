package com.example.tesis;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SesionFragment extends Fragment  {


    EditText cajauser, cajapwd;
    Button btnsesion;
    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View vista = inflater.inflate(R.layout.fragment_sesion, container, false);
        cajauser = (EditText) vista.findViewById(R.id.editTextTextPersonName);
        cajapwd = (EditText) vista.findViewById(R.id.editTextTextPersonpwd);
        btnsesion = (Button) vista.findViewById(R.id.btnIniciarSesion);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();


        btnsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailuser = cajauser.getText().toString().trim();
                String pasword = cajapwd.getText().toString().trim();
                String nameuser= "johan";

                if(emailuser.isEmpty() && pasword.isEmpty()){

                    Toast.makeText(getContext(),"Complete los datos " +
                            cajauser.getText().toString(),Toast.LENGTH_SHORT).show();


                }else {
                    registerUser(nameuser,emailuser,pasword);

                }


            }

        });

        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_sesion, container, false);
        return vista;

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
                        getActivity().onBackPressed();


                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(),"Error al guardar " +
                                cajauser.getText().toString(),Toast.LENGTH_SHORT).show();
                    }
                });


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(),"Error al registrar" +
                        cajauser.getText().toString(),Toast.LENGTH_SHORT).show();

            }
        });

    }



}