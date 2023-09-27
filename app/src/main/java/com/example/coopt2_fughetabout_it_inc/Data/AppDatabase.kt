package com.example.coopt2_fughetabout_it_inc.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Note::class, Category::class, Reminder::class], version = 1)
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
            return instance ?: synchronized(this) {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, "app_database"
                ).build()
                instance = db
                db
            }
        }
    }
}
