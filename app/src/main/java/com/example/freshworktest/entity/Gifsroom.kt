package com.example.freshworktest.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Gifsroom(
    @PrimaryKey(autoGenerate = false) var id:String,
    @ColumnInfo(name = "url") var url:String)