package com.example.tesis.ui.dashboard;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tesis.R;
import com.example.tesis.adapter.ServicioAdapter;
import com.example.tesis.adapter.ServicioAdapterdasboard;
import com.example.tesis.databinding.FragmentDashboardBinding;
import com.example.tesis.model.Servicio;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private FragmentDashboardBinding binding;


    SearchView searchView;

    Button prueba, filtroestrellas,filtrosolicitados,filtroprovedores;

    RecyclerView mRecycler;
    ServicioAdapterdasboard mAdapter;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;
    View v;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDashboard;
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });



        searchView = root.findViewById(R.id.Search);
        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        prueba = root.findViewById(R.id.buttonprueba);
        filtroestrellas = root.findViewById(R.id.filtroestrellas);





        setUApRecyclerView(root);
        search_View();

        prueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pruebameto();
            }
        });

        filtroestrellas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {filtroporpromedio();   }
        });



        return root;

    }




    @SuppressLint("NotifyDataSetChanged")
    private void setUApRecyclerView(View root) {
        mRecycler = root.findViewById(R.id.RecyclerViewServiciosdasboard);
        mRecycler.setLayoutManager(new LinearLayoutManager(root.getContext()));

        Query query = mFirestore.collection("servicio");

        FirestoreRecyclerOptions<Servicio> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Servicio>()
                        .setQuery(query.orderBy("FechaDeCreacion", Query.Direction.DESCENDING), Servicio.class).build();

        mAdapter = new ServicioAdapterdasboard(firestoreRecyclerOptions,getActivity());
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);

    }

    private void search_View() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                textSearch(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                textSearch(s);
                return false;
            }
        });

}


    private void textSearch(String s) {

        Query query = mFirestore.collection("servicio");

        FirestoreRecyclerOptions<Servicio> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Servicio>()
                        .setQuery(query.orderBy("nombre").startAt(s).endAt(s+"~")
                                , Servicio.class).build();

        mAdapter = new ServicioAdapterdasboard(firestoreRecyclerOptions,getActivity());
        mAdapter.startListening();
        mRecycler.setAdapter(mAdapter);



    }


    private void pruebameto() {
        Query query = mFirestore.collection("servicio");


        FirestoreRecyclerOptions<Servicio> firestoreRecyclerOptions =
                new FirestoreRecyclerOptions.Builder<Servicio>()
                        .setQuery(query.orderBy("FechaDeCreacion", Query.Direction.DESCENDING), Servicio.class).build();


        mAdapter = new ServicioAdapterdasboard(firestoreRecyclerOptions,getActivity());
        mAdapter.startListening();

        mRecycler.setAdapter(mAdapter);


    }

    private void filtroporpromedio() {



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