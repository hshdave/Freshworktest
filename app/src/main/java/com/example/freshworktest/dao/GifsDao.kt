package com.example.freshworktest.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.freshworktest.entity.Gifsroom

@Dao
interface GifsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(gifsroom: Gifsroom)

    @Query("DELETE FROM Gifsroom WHERE id = :id")
    fun deleteGifs(id : String)

    @Query("SELECT * FROM Gifsroom")
    fun getAllGifs(): List<Gifsroom>
}