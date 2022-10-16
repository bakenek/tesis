package com.example.tesis.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesis.R;
import com.example.tesis.model.Servicio;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class ServicioAdapterdasboard extends FirestoreRecyclerAdapter<Servicio, ServicioAdapterdasboard.ViewHolder> {

String idcreador, idc;

    FirebaseAuth mAuth;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ServicioAdapterdasboard(@NonNull FirestoreRecyclerOptions<Servicio> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Servicio model) {


        holder.nombre.setText(model.getNombre());
        holder.descripcion.setText(model.getDescripcion());


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_servicio_single, parent, false);
        return new ViewHolder(v);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView nombre,descripcion;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mAuth = FirebaseAuth.getInstance();

            nombre = itemView.findViewById(R.id.nombremiservicio);
            descripcion = itemView.findViewById(R.id.descipcionmiservicio);



        }
    }
}
