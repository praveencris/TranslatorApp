package com.example.googletranslatordemo.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "language")
public class Language {
    @NonNull
    @PrimaryKey
    private String id;
    private String english;
    private String hindi;
    private String tamil;
    private String telugu;
    private String bengali;
    private String marathi;
    private String kannada;
    private String gujarati;


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

    public String getHindi() {
        return hindi;
    }

    public void setHindi(String hindi) {
        this.hindi = hindi;
    }

    public String getTamil() {
        return tamil;
    }

    public void setTamil(String tamil) {
        this.tamil = tamil;
    }

    public String getTelugu() {
        return telugu;
    }

    public void setTelugu(String telugu) {
        this.telugu = telugu;
    }

    public String getBengali() {
        return bengali;
    }

    public void setBengali(String bengali) {
        this.bengali = bengali;
    }

    public String getMarathi() {
        return marathi;
    }

    public void setMarathi(String marathi) {
        this.marathi = marathi;
    }

    public String getKannada() {
        return kannada;
    }

    public void setKannada(String kannada) {
        this.kannada = kannada;
    }

    public String getGujarati() {
        return gujarati;
    }

    public void setGujarati(String gujarati) {
        this.gujarati = gujarati;
    }
}
