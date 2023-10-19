package com.example.coopt2_fughetabout_it_inc


import com.example.coopt2_fughetabout_it_inc.data.CategoriesViewModel
import CategoriesViewModelFactory
import com.example.coopt2_fughetabout_it_inc.data.NotesViewModel
import NotesViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.coopt2_fughetabout_it_inc.composables.NotesAppUI
import androidx.lifecycle.ViewModelProvider
import com.example.coopt2_fughetabout_it_inc.data.AppDatabase
import com.example.coopt2_fughetabout_it_inc.data.CategoryDao
import com.example.coopt2_fughetabout_it_inc.data.NoteDao


class MainActivity : ComponentActivity() {
    private lateinit var noteDao: NoteDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var categoriesViewModel: CategoriesViewModel

    /**
     * The main activity for the notes app.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the AppDatabase and get the NoteDao instance
        val appDatabase = AppDatabase.getDatabase(applicationContext)
        noteDao = appDatabase.noteDao()
        categoryDao = appDatabase.categoryDao()

        notesViewModel = ViewModelProvider(
            this,
            NotesViewModelFactory(noteDao)
        )[NotesViewModel::class.java]

        categoriesViewModel = ViewModelProvider(
            this,
            CategoriesViewModelFactory(categoryDao)
        )[CategoriesViewModel::class.java]


        // Set the content view with NotesAppUI
        setContent {
            NotesAppUI(
                noteDao = noteDao,
                categoryDao = categoryDao,
            )
        }
    }

}