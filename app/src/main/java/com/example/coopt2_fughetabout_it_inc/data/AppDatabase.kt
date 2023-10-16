package com.example.coopt2_fughetabout_it_inc.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class, Category::class, Reminder::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    // dao objects
    abstract fun noteDao(): NoteDao
    abstract fun categoryDao(): CategoryDao
    abstract fun reminderDao(): ReminderDao

    companion object {
        // volatile indicates that writes to this property are immediately visible to other threads
        @Volatile
        private var instance: AppDatabase? = null

        // getDatabase provides access to the DB
        fun getDatabase(context: Context): AppDatabase {
            // Check if an instance already exists
            println("yo it got")
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            // Create and return the database instance
            println("yo it built")
            return Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "app_database2"
            ).build()
        }
    }
}