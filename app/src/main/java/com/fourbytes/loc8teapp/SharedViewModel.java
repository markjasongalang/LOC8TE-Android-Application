package com.fourbytes.loc8teapp;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Pair> selectedData = new MutableLiveData<>();

    public void setData(Pair data) {
        selectedData.setValue(data);
    }

    public LiveData<Pair> getData() {
        return selectedData;
    }
}
