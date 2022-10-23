package com.example.tesis.ui.home;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tesis.ActualizarUsuario;
import com.example.tesis.First_layout;
import com.example.tesis.R;
import com.example.tesis.databinding.FragmentHomeBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    Button cerrarsesion, perfil;
    ImageView fotoudesuario;

    TextView nombre;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        homeViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textHome;
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        cerrarsesion = root.findViewById(R.id.btncerrarsesion);
        nombre = root.findViewById(R.id.nombredeusuario);
        perfil = root.findViewById(R.id.datosdeusuatio);
        fotoudesuario = root.findViewById(R.id.fotoUsuario);

        obtenerdatos();


        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ActualizarUsuario.class));

            }
        });

        cerrarsesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();

                //finish();

                startActivity(new Intent(getActivity(), First_layout.class));

                Toast.makeText(getContext(),"Sesion Cerrada",Toast.LENGTH_SHORT).show();

            }
        });

        return root;
    }

    private void obtenerdatos() {
        String b = mAuth.getCurrentUser().getUid();
        mFirestore.collection("user").document(b).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    String usuario = documentSnapshot.getString("nombre");
                    String fotouser = documentSnapshot.getString("Photo");
                    nombre.setText(usuario);
                    try {
                        if(!fotouser.equals("")){

                            Picasso.with(getContext())
                                    .load(fotouser)
                                    .resize(150,150)
                                    .into(fotoudesuario);

                        }

                    }catch (Exception e){
                        Log.v("Error", "e: "+ e);
                        Toast.makeText(getContext(),"Error", Toast.LENGTH_SHORT).show();



                    }



                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}