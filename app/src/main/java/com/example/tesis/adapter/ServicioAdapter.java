package com.example.tesis.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesis.AgregarServicio;
import com.example.tesis.MainActivity;
import com.example.tesis.R;
import com.example.tesis.model.Servicio;
import com.example.tesis.ui.dashboard.ViewActivityDashboard;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.lang.annotation.Documented;

public class ServicioAdapter extends FirestoreRecyclerAdapter<Servicio,ServicioAdapter.ViewHolder> {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;
    Activity activity;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */


    public ServicioAdapter(@NonNull FirestoreRecyclerOptions<Servicio> options , Activity activity) {

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
        Double estrellas = model.getEstrellas();
        Float prom = estrellas.floatValue();
        holder.ratingBar.setRating(prom);


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



        holder.g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, AgregarServicio.class);
                i.putExtra("id_servicio", id);
                activity.startActivity(i);


            }
        });

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
        RatingBar ratingBar;

        View g;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mAuth = FirebaseAuth.getInstance();

            fotodelservico = itemView.findViewById(R.id.fotodelservicio);
            nombre = itemView.findViewById(R.id.nombremiservicio);
            descripcion = itemView.findViewById(R.id.descipcionmiservicio);
            ratingBar = itemView.findViewById(R.id.ratingBarsingle);



            g=itemView;






        }
    }
}
