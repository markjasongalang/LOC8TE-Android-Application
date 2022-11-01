package com.fourbytes.loc8teapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> selectedData = new MutableLiveData<>();

    public void setData(String data) {
        selectedData.setValue(data);
    }

    public LiveData<String> getData() {
        return selectedData;
    }
}
