package com.example.tesis.ui.actividad;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tesis.databinding.FragmentActividadBinding;

public class actividadFragment extends Fragment {

    private actividadViewModel actividadViewModel;
    private FragmentActividadBinding binding;

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
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}