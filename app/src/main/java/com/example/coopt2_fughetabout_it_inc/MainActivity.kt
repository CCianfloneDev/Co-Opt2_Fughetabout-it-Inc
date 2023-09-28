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
import com.example.coopt2_fughetabout_it_inc.Data.Note
import com.example.coopt2_fughetabout_it_inc.composables.NotesAppUI
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.coopt2_fughetabout_it_inc.Data.NoteDao

class MainActivity : ComponentActivity() {
    private lateinit var notesViewModel: NotesViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContent {
//            NoteCard(Note(title = "boo", content = "Jetpack Compose", categoryId = null, reminderId =  null))
//        }
        val noteDao = NoteDao()
        notesViewModel = ViewModelProvider(this, NotesViewModelFactory(noteDao)).get(NotesViewModel::class.java)


        setContent {
            NotesAppUI(
                notes = notesViewModel.allNotes.value ?: emptyList(),
                onAddNoteClick = {
                    // Handle the click action to add a new note
                    val newNote = Note(
                        title = "New Note Title",
                        content = "New Note Content",
                        categoryId = null,
                        reminderId = null
                    )
                    notesViewModel.insertOrUpdate(newNote)
                }
            )
        }
        setContent {
            NotesAppUI(notes = getNotes()) {
                // Handle the click action to add a new note
            }
        }
    }

    // Define a function to fetch the list of notes
    private fun getNotes(): List<Note> {
        // Implement your logic to retrieve the notes from your data source
        return listOf(
            Note(1, "Note 1", "content", null, null),
            Note(2, "Note 2", "content", null, null),
            // Add more notes as needed
        )
    }
}


@Composable
fun NoteCard(msg: Note) {
    Column {
        Text(text = msg.title)
        Text(text = msg.content)
    }
}

@Preview
@Composable
fun PreviewNoteCard() {
    NoteCard(
        msg = Note(title = "Lexi", content = "Compose",categoryId = null, reminderId =  null)
    )
}