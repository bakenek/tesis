package com.example.tesis.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tesis.R;

public class ViewActivityDashboard extends AppCompatActivity {

    ImageView imageView;
    TextView titulo,descripcion, textviewgeneral;

    String id = getIntent().getStringExtra("id_servicio");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_dashboard);

        textviewgeneral = findViewById(R.id.TextViewcreadoFechaContacto);
        titulo = findViewById(R.id.nombreserviciovista);
        descripcion = findViewById(R.id.descipcionserviciovista);

        imageView = findViewById(R.id.imageViewserviciovista);


        if(id == null || id ==""){





        }else{



        }




    }
}