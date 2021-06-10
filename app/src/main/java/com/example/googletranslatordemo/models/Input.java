package com.example.googletranslatordemo.models;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.googletranslatordemo.BR;

public class Input extends BaseObservable {
    private String input;

    @Bindable
    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
        notifyPropertyChanged(BR.input);
    }
}
