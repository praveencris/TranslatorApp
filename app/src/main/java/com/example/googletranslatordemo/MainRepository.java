package com.example.googletranslatordemo;

import android.app.Application;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.googletranslatordemo.database.AppDatabase;
import com.example.googletranslatordemo.database.Language;
import com.example.googletranslatordemo.database.LanguageDao;
import com.example.googletranslatordemo.models.Result;
import com.example.googletranslatordemo.models.Translate;
import com.example.googletranslatordemo.models.TranslateResponse;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.mlkit.nl.translate.TranslateLanguage;
import com.google.mlkit.nl.translate.Translator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

interface RepositoryCallback<T> {
    void onComplete(Result<T> result);
}

public class MainRepository {
    private final ExecutorService executors;
    private final Handler handler;
    Translator toHindiTranslator, toTamilTranslator, toTeluguTranslator,
            toBengaliTranslator, toMarathiTranslator, toKannadaTranslator, toGujarati;

    private LanguageDao languageDao;

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
        languageDao = AppDatabase.getInstance(application).languageDao();
    }

    public void performTranslation(final String input, String languageCode,
                                   final RepositoryCallback<TranslateResponse> callback) {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    translateInput(input, languageCode, callback);
                } catch (Exception e) {
                    Result<TranslateResponse> errorResult = new Result.Error<>(e);
                    notifyResult(errorResult, callback);
                }
            }
        });
    }

    private void translateInput(String input, String languageCode, RepositoryCallback<TranslateResponse> callback) {
        Translator translator;
        switch (languageCode) {
            case TranslateLanguage.TAMIL:
                translator = toTamilTranslator;
                break;
            case TranslateLanguage.TELUGU:
                translator = toTeluguTranslator;
                break;
            case TranslateLanguage.BENGALI:
                translator = toBengaliTranslator;
                break;
            case TranslateLanguage.MARATHI:
                translator = toMarathiTranslator;
                break;
            case TranslateLanguage.KANNADA:
                translator = toKannadaTranslator;
                break;
            case TranslateLanguage.GUJARATI:
                translator = toGujarati;
                break;
            default:
                translator = toHindiTranslator;
                break;
        }
        translator.translate(input)
                .addOnSuccessListener(
                        new OnSuccessListener<String>() {
                            @Override
                            public void onSuccess(String translatedText) {
                                Log.d("TAG", "Translated Text: " + translatedText);
                                notifyResult(new Result.Success<>(new TranslateResponse(translatedText, languageCode)), callback);
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
    }


    private void notifyResult(Result<TranslateResponse> result, RepositoryCallback<TranslateResponse> callback) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                callback.onComplete(result);
            }
        });
    }


    public void translateToAll(final List<Translate> translateInputs,
                               final RepositoryCallback<List<Language>> callback) {
        executors.execute(new Runnable() {
            @Override
            public void run() {
                List<Language> languages = new ArrayList<>();
                for (Translate input : translateInputs) {
                    Language language = new Language();
                    language.setId(input.getId());
                    language.setEnglish(input.getEnglishText());
                }
                languageDao.insertAll(languages);
                languages.clear();
                executors.execute(new Runnable() {
                    @Override
                    public void run() {
                        for (Translate input : translateInputs) {
                            translate(input,TranslateLanguage.HINDI);
                            translate(input,TranslateLanguage.TAMIL);
                            translate(input,TranslateLanguage.TELUGU);
                            translate(input,TranslateLanguage.BENGALI);
                            translate(input,TranslateLanguage.MARATHI);
                            translate(input,TranslateLanguage.KANNADA);
                            translate(input,TranslateLanguage.GUJARATI);
                        }
                    }
                });
            }
        });
    }

    private void translate(Translate input,String languageCode) {
        translateInput(input.getEnglishText(), languageCode, new RepositoryCallback<TranslateResponse>() {
            @Override
            public void onComplete(Result<TranslateResponse> result) {
                executors.execute(new Runnable() {
                    @Override
                    public void run() {
                        String output;
                        if (result instanceof Result.Success) {
                            TranslateResponse response = ((Result.Success<TranslateResponse>) result).data;
                            output = response.getOutput();
                        } else {
                            String errorMessage = ((Result.Error<TranslateResponse>) result).exception.getMessage();
                            output = "EEE - " + errorMessage;
                        }
                        switch (languageCode) {
                            case TranslateLanguage.TAMIL:
                                languageDao.updateTamil(input.getId(), output);
                                break;
                            case TranslateLanguage.TELUGU:
                                languageDao.updateTelugu(input.getId(), output);

                                break;
                            case TranslateLanguage.BENGALI:
                                languageDao.updateBengali(input.getId(), output);
                                break;
                            case TranslateLanguage.MARATHI:
                                languageDao.updateMarathi(input.getId(), output);
                                break;
                            case TranslateLanguage.KANNADA:
                                languageDao.updateKannada(input.getId(), output);
                                break;
                            case TranslateLanguage.GUJARATI:
                                languageDao.updateGujarati(input.getId(), output);
                                break;
                            default:
                                languageDao.updateHindi(input.getId(), output);
                                break;
                        }
                    }
                });
            }
        });
    }
}
