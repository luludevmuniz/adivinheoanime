package com.alpaca.adivinheoanime.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.alpaca.adivinheoanime.database.dao.PerfomanceDao
import com.alpaca.adivinheoanime.model.Perfomance

@Database(entities = [Perfomance::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun perfomanceDao(): PerfomanceDao

    companion object {
        fun instacia(context: Context) : AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "adivinheoanime.db")
                .allowMainThreadQueries().fallbackToDestructiveMigration().build()
        }
    }
}