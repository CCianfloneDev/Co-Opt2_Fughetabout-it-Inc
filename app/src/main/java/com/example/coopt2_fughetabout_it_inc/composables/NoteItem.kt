package com.example.coopt2_fughetabout_it_inc.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.coopt2_fughetabout_it_inc.data.Note


/**
 * Composable for displaying a single note item.
 *
 * @param note The note to display.
 * @param onItemClick The action to perform when the note is clicked.
 */
@Composable
fun NoteItem(
    note: Note,
    onItemClick: (Note) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp, horizontal = 15.dp)
            .background(Color.White)
            .clickable(onClick = { onItemClick(note) }),
        elevation = 4.dp
    ) {
        Row(Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
            Column {
                Text(
                    text = "Title: " + note.title,
                    style = MaterialTheme.typography.body1,
                )
                Text(
                    text = "Content: " + note.content,
                    style = MaterialTheme.typography.body1
                )
            }
        }
    }
}