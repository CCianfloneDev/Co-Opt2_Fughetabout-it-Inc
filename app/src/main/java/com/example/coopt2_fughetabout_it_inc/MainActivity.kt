package com.example.coopt2_fughetabout_it_inc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.coopt2_fughetabout_it_inc.composables.NotesAppUI
import com.example.coopt2_fughetabout_it_inc.data.AppDatabase
import com.example.coopt2_fughetabout_it_inc.data.CategoryDao
import com.example.coopt2_fughetabout_it_inc.data.NoteDao

class MainActivity : ComponentActivity() {
    // Data Access Objects
    private lateinit var noteDao: NoteDao
    private lateinit var categoryDao: CategoryDao

    /**
     * The main activity for the notes app.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the AppDatabase and get the NoteDao instance
        val appDatabase = AppDatabase.getDatabase(applicationContext)
        noteDao = appDatabase.noteDao()
        categoryDao = appDatabase.categoryDao()

        // Set the content view with NotesAppUI
        setContent {
            // pass the DAO's to the UI for further use.
            NotesAppUI(
                noteDao = noteDao,
                categoryDao = categoryDao,
            )
        }
    }
}