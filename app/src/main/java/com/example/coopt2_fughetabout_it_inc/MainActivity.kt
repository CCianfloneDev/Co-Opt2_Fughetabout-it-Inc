package com.example.coopt2_fughetabout_it_inc


import NotesViewModel
import NotesViewModelFactory
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Observer
import com.example.coopt2_fughetabout_it_inc.Data.Note
import com.example.coopt2_fughetabout_it_inc.composables.NotesAppUI
import androidx.lifecycle.ViewModelProvider
import com.example.coopt2_fughetabout_it_inc.Data.AppDatabase
import com.example.coopt2_fughetabout_it_inc.Data.CategoryDao
import com.example.coopt2_fughetabout_it_inc.Data.NoteDao
import com.example.coopt2_fughetabout_it_inc.Data.ReminderDao


class MainActivity : ComponentActivity() {
    private lateinit var noteDao: NoteDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var reminderDao: ReminderDao
    private lateinit var notesViewModel: NotesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the AppDatabase and get the NoteDao instance
        val appDatabase = AppDatabase.getDatabase(applicationContext)
        noteDao = appDatabase.noteDao()
        categoryDao = appDatabase.categoryDao()
        reminderDao = appDatabase.reminderDao()

        // Initialize the ViewModel
        notesViewModel = ViewModelProvider(
            this,
            NotesViewModelFactory(noteDao, categoryDao, reminderDao)
        ).get(NotesViewModel::class.java)
        val allNotes = noteDao.getAllNotes()
        val allCategories = categoryDao.getAllCategories()
        val allReminders = reminderDao.getAllReminders()

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
                notes = allNotes,
                categories = allCategories,
                reminders = allReminders
            )
        }
    }

//    // Define a function to fetch the list of notes
//    private fun getNotes(): List<Note> {
//        // Implement your logic to retrieve the notes from your data source
//        return listOf(
//            Note(1, "Note 1", "content", null, null),
//            Note(2, "Note 2", "content", null, null),
//            // Add more notes as needed
//        )
//    }
}


//@Composable
//fun NoteCard(msg: Note) {
//    Column {
//        Text(text = msg.title)
//        Text(text = msg.content)
//    }
//}
//
//@Preview
//@Composable
//fun PreviewNoteCard() {
//    NoteCard(
//        msg = Note(title = "Lexi", content = "Compose",categoryId = null, reminderId =  null)
//    )
//}