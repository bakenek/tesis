package com.example.tesis.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesis.R;
import com.example.tesis.User;
import com.example.tesis.model.Servicio;
import com.example.tesis.model.usuario;
import com.example.tesis.ui.dashboard.ViewActivityDashboard;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class usuarioAdapter extends FirestoreRecyclerAdapter<usuario, usuarioAdapter.ViewHolder2> {

    FirebaseAuth mAuth;
    Activity activity;
    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public usuarioAdapter(@NonNull FirestoreRecyclerOptions<usuario> options,Activity activity) {

        super(options);
        this.activity = activity;
    }



    @Override
    protected void onBindViewHolder(@NonNull usuarioAdapter.ViewHolder2 holder, int position, @NonNull usuario model) {

        DocumentSnapshot documentSnapshot = getSnapshots().getSnapshot(holder.getAbsoluteAdapterPosition());

        final String id = documentSnapshot.getId();

        holder.nombre.setText(model.getNombre());
        String correo = model.getCorreo();
        String contacto = model.getContacto();
        String desp = "Correo: " + correo + "\n" + "Contacto: " + contacto + " ";
        holder.descripcion.setText(desp);

        Double estrellas = model.getEstrellas();
        Float promo = estrellas.floatValue();
        holder.ratingBar.setRating(promo);

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

                Intent i = new Intent(activity, User.class);
                i.putExtra("idcreador", id);
                activity.startActivity(i);

            }
        });


    }

    @NonNull
    @Override
    public usuarioAdapter.ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_servicio_single, parent, false);
        return new ViewHolder2(v);

    }

    public class ViewHolder2 extends RecyclerView.ViewHolder{

        TextView nombre,descripcion;
        ImageView fotodelservico;
        RatingBar ratingBar;

        View d;

        public ViewHolder2(@NonNull View itemView) {
            super(itemView);

            mAuth = FirebaseAuth.getInstance();

            fotodelservico = itemView.findViewById(R.id.fotodelservicio);
            nombre = itemView.findViewById(R.id.nombremiservicio);
            descripcion = itemView.findViewById(R.id.descipcionmiservicio);
            ratingBar = itemView.findViewById(R.id.ratingBarsingle);


            d =itemView;


        }
    }
}
