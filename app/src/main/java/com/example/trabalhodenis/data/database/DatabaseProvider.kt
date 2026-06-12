package com.example.trabalhodenis.data.database

import android.content.Context
import androidx.room.Room
import com.example.trabalhodenis.data.local.AppDatabase

object DatabaseProvider {

    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "mindflow_db"
            ).build()

            INSTANCE = instance
            instance
        }
    }
}
