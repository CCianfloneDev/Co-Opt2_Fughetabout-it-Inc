package com.example.coopt2_fughetabout_it_inc.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.coopt2_fughetabout_it_inc.Data.Note
import com.example.coopt2_fughetabout_it_inc.Data.Category
import com.example.coopt2_fughetabout_it_inc.Data.Reminder
@Composable
fun <T> LiveData<T>.observeAsState(initial: T): T {
    val liveData = this
    val state = remember { mutableStateOf(initial) }

    DisposableEffect(liveData) {
        val observer = androidx.lifecycle.Observer<T> { value ->
            state.value = value
        }
        liveData.observeForever(observer)

        onDispose {
            liveData.removeObserver(observer)
        }
    }

    return state.value
}
@Composable
fun NotesAppUI(
    notes: LiveData<List<Note>>,
    categories: LiveData<List<Category>>,
    reminders: LiveData<List<Reminder>>
) {
    val notesList = notes.observeAsState(emptyList())

    // State to track whether the user is creating a new note
    var isCreatingNote by remember { mutableStateOf(false) }
    var isCreatingCategory by remember { mutableStateOf(false) }
    var isCreatingReminder by remember { mutableStateOf(false) }
    var selectedNote: Note? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Title
        Text(
            text = "My Notes",
            style = MaterialTheme.typography.h4,
            modifier = Modifier.padding(16.dp)
        )

        // LazyColumn to display notes
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(notesList) { note ->
                // Click on a note to open NoteCreationScreen
                NoteItem(note = note)
                {
                    selectedNote = note
                    isCreatingNote = true
                }
            }
        }

        // Button to add a new note
        Button(
            onClick = {
                selectedNote = null // Clear the selected note
                isCreatingNote = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            Text(text = "Add New Note")
        }

        // Show the note creation screen when isCreatingNote is true
        if (isCreatingNote) {
            NoteCreationScreen(
                note = selectedNote,
                onNoteCreated = { newNote ->
                    // Handle note creation and save to the database
                    // You can call a ViewModel function here to save the note
                    // After saving, set isCreatingNote back to false
                    selectedNote = null
                    isCreatingNote = false
                },
                onCancel = {
                    selectedNote = null
                    isCreatingNote = false
                },
                onCategoryCreate = {
                    isCreatingCategory = true
                },
                onReminderCreate = {
                    println("mexico")
                    isCreatingReminder = true
                },
                onDelete = {
                    // handle deleting of the note here
                    selectedNote = null
                    isCreatingNote = false
                }
            )
        }

        if (isCreatingCategory) {
            println("mexico2")
            CategorySelectionScreen(
                categories = categories,
                selectedCategoryId = selectedNote?.categoryId,
                onCategorySelected = { categoryId ->
                    // Handle category selection and update the categoryId in the note
                    selectedNote?.categoryId = categoryId
                    isCreatingNote = true // Return to the NoteCreationScreen
                },
                onCategoryCreated = { categoryName ->
                    // Handle category creation and update the categoryId in the note
                    // Here, you might want to add the new category to your database
                    //val newCategoryId = createNewCategory(categoryName) // Implement this function
                   // selectedNote?.categoryId = newCategoryId
                    isCreatingNote = true // Return to the NoteCreationScreen
                },
                onCancel = {
                    isCreatingCategory = false
                }
            )
        }

    }
}

@Composable
fun NoteCreationScreen(
    note: Note?, // nullable note parameter incase we are opening a note instead of creaating
    onNoteCreated: (Note) -> Unit,
    onCancel: () -> Unit,
    onCategoryCreate: () -> Unit,
    onReminderCreate: () -> Unit,
    onDelete: () -> Unit
) {
    // Define state variables for user input
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var categoryId by remember { mutableStateOf<Long?>(null) }
    var reminderId by remember { mutableStateOf<Long?>(null) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Input fields for title, content, category, and reminder
        TextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier.fillMaxWidth()
        )

        // Button to select or create a category
        Button(
            onClick = { onCategoryCreate() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select/Create Category")
        }

        // Button to select or create a reminder
        Button(
            onClick = { onReminderCreate() },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Select/Create Reminder")
        }

        // Save, cancel, and delete buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    val newNote = Note(
                        id = note?.id ?: 0L, // Pass the id if it's an existing note
                        title = title,
                        content = content,
                        categoryId = categoryId,
                        reminderId = reminderId
                    )
                    onNoteCreated(newNote)
                },
            ) {
                Text("Save")
            }

            // Cancel button
            Button(
                onClick = onCancel,
            ) {
                Text("Cancel")
            }

            // Delete button (show only if it's an existing note)
            if (note != null) {
                Button(
                    onClick = onDelete,
                ) {
                    Text("Delete")
                }
            }
        }
    }

}

@Composable
fun CategorySelectionScreen(
    categories: LiveData<List<Category>>,
    selectedCategoryId: Long?,
    onCategorySelected: (Long) -> Unit,
    onCategoryCreated: (String) -> Unit,
    onCancel: () -> Unit
) {
    val categoriesList = categories.observeAsState(emptyList())
    var newCategoryName by remember { mutableStateOf("") }
    var isCreatingNewCategory by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = "Select/Create Category",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Dropdown to select an existing category
        DropdownMenu(
            expanded = isCreatingNewCategory.not(),
            onDismissRequest = { isCreatingNewCategory = false }
        ) {
            categoriesList.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        onCategorySelected(category.id)
                        isCreatingNewCategory = false
                    }
                ) {
                    Text(text = category.name)
                }
            }
        }

        // Button to show text field for creating a new category
        Button(
            onClick = { isCreatingNewCategory = true },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Add New Category")
        }

        if (isCreatingNewCategory) {
            // Text field for entering a new category name
            TextField(
                value = newCategoryName,
                onValueChange = { newCategoryName = it },
                label = { Text("Category Name") },
                modifier = Modifier.fillMaxWidth()
            )

            // Save and Cancel buttons for creating a new category
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        if (newCategoryName.isNotBlank()) {
                            onCategoryCreated(newCategoryName)
                            isCreatingNewCategory = false
                            newCategoryName = ""
                        }
                    }
                ) {
                    Text("Save")
                }

                Button(
                    onClick = {
                        isCreatingNewCategory = false
                        newCategoryName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        }

        // Cancel button to return to the NoteCreationScreen
        Button(
            onClick = onCancel,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Cancel and Return to Note Creation")
        }
    }
}

@Composable
fun NoteItem(
    note: Note,
    onItemClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .clickable(onClick = onItemClick)
        ) {
            Text(
                text = note.content,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}