package com.example.tesis.ui.actividad;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

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
