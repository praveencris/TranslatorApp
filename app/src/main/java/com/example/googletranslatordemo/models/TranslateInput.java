package com.example.googletranslatordemo.models;

public class TranslateInput {
    private String id;
    private String english;

    public TranslateInput(String id, String english) {
        this.id = id;
        this.english = english;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEnglish() {
        return english;
    }

    public void setEnglish(String english) {
        this.english = english;
    }
}
