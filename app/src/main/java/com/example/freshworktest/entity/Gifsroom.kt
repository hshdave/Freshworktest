package com.example.freshworktest.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


@Entity
data class Gifsroom(
    @PrimaryKey(autoGenerate = false) var id:String,
    @ColumnInfo(name = "url") var url:String)