package com.example.freshworktest.dao

import androidx.room.*
import com.example.freshworktest.entity.Gifsroom
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface GifsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gifsroom: Gifsroom)

    @Query("DELETE FROM Gifsroom WHERE id = :id")
    fun deleteGifs(id : String)

    @Query("SELECT * FROM Gifsroom")
    fun getAllGifs(): List<Gifsroom>
}