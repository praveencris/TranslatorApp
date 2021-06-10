package com.example.googletranslatordemo.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.googletranslatordemo.BR;

public class Language extends BaseObservable {
    private int selectedItemPosition;

    @Bindable
    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelectedItemPosition(int selectedItemPosition) {
        this.selectedItemPosition = selectedItemPosition;
        notifyPropertyChanged(BR.selectedItemPosition);
    }
}
