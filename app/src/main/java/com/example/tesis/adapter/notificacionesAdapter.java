package com.example.tesis.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;


import com.example.tesis.R;


import com.example.tesis.model.notificaciones;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class notificacionesAdapter extends FirestoreRecyclerAdapter<notificaciones,notificacionesAdapter.ViewHolder> {

    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public notificacionesAdapter(@NonNull FirestoreRecyclerOptions<notificaciones> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull notificaciones model) {
        holder.titulo.setText(model.getTitulo());
        holder.cuerpo.setText(model.getCuerpo());
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_notificaciones_single,
                parent , false);


        return new ViewHolder(v);
    }



    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView titulo,cuerpo;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mAuth = FirebaseAuth.getInstance();
          titulo = itemView.findViewById(R.id.textViewTitulonotificacionn);
           cuerpo = itemView.findViewById(R.id.textViewcuerponotificacion);




        }
    }
}
