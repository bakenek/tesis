package com.example.tesis.ui.actividad;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class actividadViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public actividadViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is actividad fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
