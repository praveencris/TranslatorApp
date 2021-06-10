package com.example.googletranslatordemo;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.googletranslatordemo.databinding.ActivityMainBinding;
import com.example.googletranslatordemo.models.Input;
import com.example.googletranslatordemo.models.Language;
import com.example.googletranslatordemo.models.Result;
import com.example.googletranslatordemo.models.TranslateResponse;


public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel mainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        binding.setInput(new Input());
        binding.setLanguage(new Language());
        binding.setLifecycleOwner(this);
        binding.setViewModel(mainViewModel);

        mainViewModel.translateResponse.observe(this, new Observer<Result<TranslateResponse>>() {
            @Override
            public void onChanged(Result<TranslateResponse> result) {
                if (result != null) {
                    if (result instanceof Result.Success) {
                        TranslateResponse translateResponse = ((Result.Success<TranslateResponse>) result).data;
                        binding.outputEt.setText(translateResponse.getOutput());
                    } else {
                        String errorMessage = ((Result.Error<TranslateResponse>) result).exception.getMessage();
                        Log.d("TAG", "Error :" + errorMessage);
                    }
                }
            }
        });
    }
}