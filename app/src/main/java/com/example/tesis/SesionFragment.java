package com.example.tesis;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class SesionFragment extends Fragment implements Response.Listener<JSONObject>,
        Response.ErrorListener {

    RequestQueue rq;
    JsonRequest   jrq;

    EditText cajauser, cajapwd;
    Button btnsesion;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista = inflater.inflate(R.layout.fragment_sesion, container, false);
        cajauser = (EditText) vista.findViewById(R.id.editTextTextPersonName);
        cajapwd = (EditText) vista.findViewById(R.id.editTextTextPersonpwd);
        btnsesion = (Button) vista.findViewById(R.id.btnIniciarSesion);
         rq = Volley.newRequestQueue(getContext());


        btnsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        // Inflate the layout for this fragment
        // return inflater.inflate(R.layout.fragment_sesion, container, false);
        return vista;

    }




    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getContext(),"no se ha encontro al usuario " +
                cajauser.getText().toString(),Toast.LENGTH_SHORT).show();





    }

    @Override
    public void onResponse(JSONObject response) {



        Toast.makeText(getContext(),"se ha encontrado al usuario " +
                cajauser.getText().toString(),Toast.LENGTH_SHORT).show();

        User usuario = new User();
        JSONArray jsonArray = response.optJSONArray("datos");
        JSONObject jsonObject = null;

        try {
            jsonObject = jsonArray.getJSONObject(0);

            usuario.setUsuario(jsonObject.optString("usuario"));
            usuario.setAlias(jsonObject.optString("alias"));
            usuario.setClave(jsonObject.optString("clave"));
            usuario.setFotoDePerfil(jsonObject.optString("fotodeperfil"));
            usuario.setContacto(jsonObject.optString("contacto"));


        } catch (JSONException e) {
            e.printStackTrace();
        }



    }


    private void iniciarSesion() {

        String url = "http://192.168.0.136/tesis/login/sesion.php?usuario="+cajauser.getText().toString()+
               "&clave="+cajapwd.getText().toString();
       // String url ="http://192.168.0.136/tesis/login/sesion.php?usuario=bebe&clave=123";



        jrq = new JsonObjectRequest(Request.Method.GET, url,null,this,this);
        rq.add(jrq);





    }

}