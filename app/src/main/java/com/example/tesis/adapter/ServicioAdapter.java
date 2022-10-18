package com.example.tesis.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesis.MainActivity;
import com.example.tesis.R;
import com.example.tesis.model.Servicio;
import com.example.tesis.ui.dashboard.ViewActivityDashboard;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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



        holder.g.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(activity, //ViewActivityDashboard.class
                        MainActivity.class);
                i.putExtra("id_servicio", id);
                activity.startActivity(i);
                Toast.makeText(activity, id, Toast.LENGTH_SHORT).show();

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

        View g;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mAuth = FirebaseAuth.getInstance();

            nombre = itemView.findViewById(R.id.nombremiservicio);
            descripcion = itemView.findViewById(R.id.descipcionmiservicio);


            g=itemView;






        }
    }
}
