package com.example.freshworktest.presenter.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.freshworktest.entity.Gifsroom
import com.example.freshworktest.dao.GifsDao

@Database(entities = [Gifsroom::class], version = 1)
@TypeConverters(DataTypeConverters::class)
abstract class AppDatabase : RoomDatabase(){

    abstract fun gifDao() : GifsDao

    companion object {
        var INSTANCE: AppDatabase? = null

        fun getAppDataBase(context: Context): AppDatabase? {
            if (INSTANCE == null){
                synchronized(AppDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "myDB").build()
                }
            }
            return INSTANCE
        }

        fun destroyDataBase(){
            INSTANCE = null
        }
    }
}