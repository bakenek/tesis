package com.example.tesis.ui.actividad;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesis.AgregarServicio;
import com.example.tesis.First_layout;
import com.example.tesis.R;
import com.example.tesis.adapter.ServicioAdapter;
import com.example.tesis.databinding.FragmentActividadBinding;
import com.example.tesis.model.Servicio;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class actividadFragment extends Fragment {

    private actividadViewModel actividadViewModel;
    private FragmentActividadBinding binding;

    Button IragregarServicio;

    RecyclerView mRecycler;
    ServicioAdapter mAdapter;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        actividadViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(actividadViewModel.class);

        binding = FragmentActividadBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textactividad;
        actividadViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });


        IragregarServicio = root.findViewById(R.id.btnIrAAgregarServicio);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = root.findViewById(R.id.RecyclerViewMisServicios);
        mRecycler.setLayoutManager(new LinearLayoutManager(root.getContext()));
        Query query = mFirestore.collection("servicio");
        String b = mAuth.getCurrentUser().getUid();

        FirestoreRecyclerOptions<Servicio> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Servicio>().setQuery(query.orderBy("iddelcreador").startAt(b).endAt(b)
                        , Servicio.class).build();

        mAdapter = new ServicioAdapter(firestoreRecyclerOptions , getActivity());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);





        IragregarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getContext(), AgregarServicio.class));
            }
        });




        return root;
    }

    @Override
    public void onStart() {
        super.onStart();

        mAdapter.startListening();

    }




    @Override
    public void onStop() {
        super.onStop();
        mAdapter.stopListening();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}