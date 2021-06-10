package com.example.googletranslatordemo;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.os.HandlerCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.common.model.DownloadConditions;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translation;
import com.google.mlkit.nl.translate.Translator;
import com.google.mlkit.nl.translate.TranslatorOptions;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyApplication extends Application {
    ExecutorService executorService = Executors.newFixedThreadPool(4);
    Handler mainThreadHandler = HandlerCompat.createAsync(Looper.getMainLooper());
    Translator toHindiTranslator, toTamilTranslator, toTeluguTranslator,
            toBengaliTranslator, toMarathiTranslator, toKannadaTranslator, toGujarati;

    @Override
    public void onCreate() {
        super.onCreate();
        toHindiTranslator = getTranslator(TranslateLanguage.HINDI);
        toTamilTranslator = getTranslator(TranslateLanguage.TAMIL);
        toTeluguTranslator = getTranslator(TranslateLanguage.TELUGU);
        toBengaliTranslator = getTranslator(TranslateLanguage.BENGALI);
        toMarathiTranslator = getTranslator(TranslateLanguage.MARATHI);
        toKannadaTranslator = getTranslator(TranslateLanguage.KANNADA);
        toGujarati = getTranslator(TranslateLanguage.GUJARATI);
    }

    /**
     * Translate from english language to other language
     *
     * @param toLanguage Translated To language
     * @return @Translator
     */
    private Translator getTranslator(String toLanguage) {
        TranslatorOptions options =
                new TranslatorOptions.Builder()
                        .setSourceLanguage(TranslateLanguage.ENGLISH)
                        .setTargetLanguage(toLanguage)
                        .build();
        final Translator englishToOtherTranslator =
                Translation.getClient(options);
        DownloadConditions conditions = new DownloadConditions.Builder()
                .requireWifi()
                .build();
        englishToOtherTranslator.downloadModelIfNeeded(conditions)
                .addOnSuccessListener(
                        new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Log.d("TAG", "Download success : English to " + toLanguage);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d("TAG", "Failed : English to " + toLanguage);
                            }
                        });
        return englishToOtherTranslator;
    }
}
