package com.example.googletranslatordemo.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LanguageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertAll(List<Language> languages);

    @Query("update language set hindi=:value where id=:id")
    public void updateHindi(String id,String value);

    @Query("update language set tamil=:value where id=:id")
    public void updateTamil(String id, String value);

    @Query("update language set telugu=:value where id=:id")
    public void updateTelugu(String id, String value);

    @Query("update language set bengali=:value where id=:id")
    public void updateBengali(String id, String value);

    @Query("update language set marathi=:value where id=:id")
    public void updateMarathi(String id, String value);

    @Query("update language set kannada=:value where id=:id")
    public void updateKannada(String id, String value);

    @Query("update language set gujarati=:value where id=:id")
    public void updateGujarati(String id, String value);

}
