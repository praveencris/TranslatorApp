package com.example.googletranslatordemo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.googletranslatordemo.models.Result;
import com.example.googletranslatordemo.models.TranslateResponse;
import com.google.mlkit.nl.translate.TranslateLanguage;

public class MainViewModel extends AndroidViewModel {
    private final MainRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new MainRepository(application);
    }

    private final MutableLiveData<Result<TranslateResponse>> _translateResponse = new MutableLiveData<>();
    LiveData<Result<TranslateResponse>> translateResponse = _translateResponse;

    public void onTranslateClicked(String input, int selectedItemPosition) {
        repository.performNetworkRequest(input, getLanguageCode(selectedItemPosition), new RepositoryCallback<TranslateResponse>() {
            @Override
            public void onComplete(Result<TranslateResponse> result) {
                _translateResponse.setValue(result);
            }
        });


    }

    private String getLanguageCode(int selectedPosition) {
        switch (selectedPosition) {
            case 0:
                return TranslateLanguage.HINDI;
            case 1:
                return TranslateLanguage.TAMIL;
            case 2:
                return TranslateLanguage.TELUGU;
            case 3:
                return TranslateLanguage.BENGALI;
            case 4:
                return TranslateLanguage.MARATHI;
            case 5:
                return TranslateLanguage.KANNADA;
            case 6:
                return TranslateLanguage.GUJARATI;
        }
        return TranslateLanguage.HINDI;
    }
}
