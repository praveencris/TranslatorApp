package com.example.googletranslatordemo;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.googletranslatordemo.models.Result;
import com.example.googletranslatordemo.models.TranslateResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}

public class MainRepository {
    private final ExecutorService executors;
    private final Handler handler;
    Translator toHindiTranslator, toTamilTranslator, toTeluguTranslator,
            toBengaliTranslator, toMarathiTranslator, toKannadaTranslator, toGujarati;

    public MainRepository(Application application) {
        MyApplication myApplication = (MyApplication) application;
        this.executors = myApplication.executorService;
        this.handler = myApplication.mainThreadHandler;
        toHindiTranslator = myApplication.toHindiTranslator;
        toTamilTranslator = myApplication.toTamilTranslator;
        toTeluguTranslator = myApplication.toTeluguTranslator;
        toBengaliTranslator = myApplication.toBengaliTranslator;
        toMarathiTranslator = myApplication.toMarathiTranslator;
        toKannadaTranslator = myApplication.toKannadaTranslator;
        toGujarati = myApplication.toGujarati;
    }

    public void performNetworkRequest(final String input,
                                      final RepositoryCallback<TranslateResponse> callback) {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    toHindiTranslator.translate(input)
                            .addOnSuccessListener(
                                    new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String translatedText) {
                                            Log.d("TAG", "Translated Text: " + translatedText);
                                            notifyResult(new Result.Success<>(new TranslateResponse(translatedText, TranslateLanguage.HINDI)), callback);
                                        }
                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Error.
                                            // ...
                                            Log.d("TAG", "Translation Failed " + e.getMessage());
                                            notifyResult(new Result.Error<>(new IOException("Translation failed for : " + input)), callback);
                                        }
                                    });
                } catch (Exception e) {
                    Result<TranslateResponse> errorResult = new Result.Error<>(e);
                    notifyResult(errorResult, callback);
                }
            }
        });
    }


    private void notifyResult(Result<TranslateResponse> result, RepositoryCallback<TranslateResponse> callback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(result);
            }
        });
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("TAG", "Error in creating url");
        }
        return url;
    }
    private static String readInputStream(InputStream inputStream) {
        StringBuilder output = new StringBuilder();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        try {
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        } catch (IOException e) {
            Log.e("TAG", "Error occurred while reading input stream");
        }
        return output.toString();
    }

}
