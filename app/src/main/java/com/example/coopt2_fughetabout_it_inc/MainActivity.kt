package com.example.coopt2_fughetabout_it_inc


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.coopt2_fughetabout_it_inc.Data.Note

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteCard(Note(title = "boo", content = "Jetpack Compose", categoryId = null, reminderId =  null))
        }
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