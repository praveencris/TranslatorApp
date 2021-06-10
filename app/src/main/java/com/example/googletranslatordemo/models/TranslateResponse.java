package com.example.googletranslatordemo.models;

public class TranslateResponse {
    private String output;
    private String language;

    public TranslateResponse(String output, String language) {
        this.output = output;
        this.language = language;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    @Override
    public String toString() {
        return "TranslateResponse{" +
                "output='" + output + '\'' +
                ", language='" + language + '\'' +
                '}';
    }
}
