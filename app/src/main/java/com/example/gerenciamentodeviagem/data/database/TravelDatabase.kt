package com.example.travelapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.travelapp.data.model.Travel

@Database(entities = [Travel::class], version = 1, exportSchema = false)
abstract class TravelDatabase : RoomDatabase() {
    abstract fun travelDao(): TravelDao

    companion object {
        @Volatile
        private var INSTANCE: TravelDatabase? = null

        fun getDatabase(context: Context): TravelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelDatabase::class.java,
                    "travel_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
