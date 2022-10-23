package com.example.tesis.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesis.ActualizarUsuario;
import com.example.tesis.MainActivity;
import com.example.tesis.R;
import com.example.tesis.model.Servicio;
import com.example.tesis.ui.dashboard.ViewActivityDashboard;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class ServicioAdapterdasboard extends FirestoreRecyclerAdapter<Servicio, ServicioAdapterdasboard.ViewHolder> {




    FirebaseAuth mAuth;
    Activity activity;


    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ServicioAdapterdasboard(@NonNull FirestoreRecyclerOptions<Servicio> options, Activity activity) {
        super(options);
        this.activity = activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Servicio model) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());

        final String id = documentSnapshot.getId();

        holder.nombre.setText(model.getNombre());
        holder.descripcion.setText(model.getDescripcion());
        String fotoservicio = model.getPhoto();


        try {
            if(!fotoservicio.equals("")){

                Picasso.with(activity.getApplicationContext())
                        .load(fotoservicio)
                        .resize(150,150)
                        .into(holder.fotodelservico);

            }

        }catch (Exception e){
            Log.v("Error", "e: "+ e);

        }



        holder.d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity,ViewActivityDashboard.class
                        );
                i.putExtra("id_serviciodasboard", id);
                activity.startActivity(i);

            }
        });


        //holder.v.set

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_servicio_single, parent, false);
        return new ViewHolder(v);

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre,descripcion;
        ImageView fotodelservico;

        View d;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            mAuth = FirebaseAuth.getInstance();

            fotodelservico = itemView.findViewById(R.id.fotodelservicio);
            nombre = itemView.findViewById(R.id.nombremiservicio);
            descripcion = itemView.findViewById(R.id.descipcionmiservicio);


            d =itemView;



        }
    }
}
