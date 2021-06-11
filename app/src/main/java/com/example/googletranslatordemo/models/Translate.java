package com.example.googletranslatordemo.models;

public class Translate {
    private String id;
    private String englishText;
    private String translatedText;

    public Translate(String id, String englishText, String translatedText) {
        this.id = id;
        this.englishText = englishText;
        this.translatedText = translatedText;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnglishText() {
        return englishText;
    }

    public void setEnglishText(String englishText) {
        this.englishText = englishText;
    }

    public String getTranslatedText() {
        return translatedText;
    }

    public void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }
}
