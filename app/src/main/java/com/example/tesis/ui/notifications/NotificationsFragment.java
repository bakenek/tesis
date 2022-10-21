package com.example.tesis.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.tesis.R;
import com.example.tesis.adapter.notificacionesAdapter;
import com.example.tesis.databinding.FragmentNotificationsBinding;
import com.example.tesis.model.notificaciones;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class NotificationsFragment extends Fragment {

    private NotificationsViewModel notificationsViewModel;
    private FragmentNotificationsBinding binding;

    RecyclerView mRecycler;
    notificacionesAdapter mAdapter;

    FirebaseAuth mAuth;
    FirebaseFirestore mFirestore;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(NotificationsViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textNotifications;
        notificationsViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        mRecycler = root.findViewById(R.id.notificacionesRecycleView);
        mRecycler.setLayoutManager(new LinearLayoutManager(root.getContext()));
        Query query = mFirestore.collection("notificaciones");
        String b = mAuth.getCurrentUser().getUid();


        FirestoreRecyclerOptions<notificaciones> firestoreRecyclerOptions = new FirestoreRecyclerOptions.Builder
                <notificaciones>().setQuery(query.orderBy("idnotificado").startAt(b).endAt(b), notificaciones.class).build();

        mAdapter = new notificacionesAdapter(firestoreRecyclerOptions);
        mAdapter.notifyDataSetChanged();
        mRecycler.setAdapter(mAdapter);









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