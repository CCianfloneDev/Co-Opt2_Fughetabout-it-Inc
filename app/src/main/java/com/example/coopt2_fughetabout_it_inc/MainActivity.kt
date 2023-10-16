package com.example.coopt2_fughetabout_it_inc


import com.example.coopt2_fughetabout_it_inc.data.CategoriesViewModel
import CategoriesViewModelFactory
import com.example.coopt2_fughetabout_it_inc.data.NotesViewModel
import NotesViewModelFactory
import com.example.coopt2_fughetabout_it_inc.data.RemindersViewModel
import RemindersViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.coopt2_fughetabout_it_inc.composables.NotesAppUI
import androidx.lifecycle.ViewModelProvider
import com.example.coopt2_fughetabout_it_inc.data.AppDatabase
import com.example.coopt2_fughetabout_it_inc.data.CategoryDao
import com.example.coopt2_fughetabout_it_inc.data.NoteDao
import com.example.coopt2_fughetabout_it_inc.data.ReminderDao


class MainActivity : ComponentActivity() {
    private lateinit var noteDao: NoteDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var reminderDao: ReminderDao
    private lateinit var notesViewModel: NotesViewModel
    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var remindersViewModel: RemindersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the AppDatabase and get the NoteDao instance
        val appDatabase = AppDatabase.getDatabase(applicationContext)
        noteDao = appDatabase.noteDao()
        categoryDao = appDatabase.categoryDao()
        reminderDao = appDatabase.reminderDao()

        notesViewModel = ViewModelProvider(
            this,
            NotesViewModelFactory(noteDao)
        )[NotesViewModel::class.java]

        categoriesViewModel = ViewModelProvider(
            this,
            CategoriesViewModelFactory(categoryDao)
        )[CategoriesViewModel::class.java]

        remindersViewModel = ViewModelProvider(
            this,
            RemindersViewModelFactory(reminderDao)
        )[RemindersViewModel::class.java]

//        val allNotes = noteDao.getAllNotes()
//        val allCategories = categoryDao.getAllCategories()
//        val allReminders = reminderDao.getAllReminders()

//        allNotes.observe(this, Observer { notes ->
//            for (note in notes) {
//                println("Note ID: ${note.id}")
//                println("Title: ${note.title}")
//                println("Content: ${note.content}")
//                // Print other properties as needed
//            }
//        })
        // Set the content view with NotesAppUI
        setContent {
            NotesAppUI(
                noteDao = noteDao,
                categoryDao = categoryDao,
                reminderDao = reminderDao,
            )
        }
    }

}