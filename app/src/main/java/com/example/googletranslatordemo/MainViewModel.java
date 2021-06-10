package com.example.googletranslatordemo;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.googletranslatordemo.models.Result;
import com.example.googletranslatordemo.models.TranslateResponse;

public class MainViewModel extends AndroidViewModel {
    private final MainRepository repository;

    public MainViewModel(@NonNull Application application) {
        super(application);
        repository = new MainRepository(application);
    }

    private MutableLiveData<Result<TranslateResponse>> _translateResponse = new MutableLiveData<>();
    LiveData<Result<TranslateResponse>> translateResponse = _translateResponse;

    public void onTranslateClicked(String input) {
        repository.performNetworkRequest(input, new RepositoryCallback<TranslateResponse>() {
            @Override
            public void onComplete(Result<TranslateResponse> result) {
                _translateResponse.setValue(result);
            }
        });
    }
}
