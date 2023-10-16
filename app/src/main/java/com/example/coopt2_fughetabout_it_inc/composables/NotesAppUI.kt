package com.example.coopt2_fughetabout_it_inc.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LiveData
import com.example.coopt2_fughetabout_it_inc.data.Category
import com.example.coopt2_fughetabout_it_inc.data.CategoryDao
import com.example.coopt2_fughetabout_it_inc.data.Note
import com.example.coopt2_fughetabout_it_inc.data.NoteDao
import com.example.coopt2_fughetabout_it_inc.data.Reminder
import com.example.coopt2_fughetabout_it_inc.data.ReminderDao
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


///Converts the notes into an observable type.
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
fun NoteItem(
    note: Note,
    onItemClick: (Note) -> Unit
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
                .clickable(onClick = { onItemClick(note) })
        ) {
            Text(
                text = note.title,
                style = MaterialTheme.typography.body1,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(DelicateCoroutinesApi::class)
@Composable
fun NotesAppUI(
    noteDao: NoteDao,
    categoryDao: CategoryDao,
    reminderDao: ReminderDao
) {
    val notesList = noteDao.getAllNotes().observeAsState(emptyList())

    // State to track whether the user is creating a new note
    var isCreatingNote by remember { mutableStateOf(false) }
    var isCreatingCategory by remember { mutableStateOf(false) }
    var isCreatingReminder by remember { mutableStateOf(false) }
    var selectedNote: Note? by remember { mutableStateOf(null) }

    // this will be set if we're creating a new category

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize()) {
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
                        isCreatingCategory = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Add New Note")
                }

                // Button to add a new note
                Button(
                    onClick = {
                        selectedNote = null // Clear the selected note
                        isCreatingNote = false
                        isCreatingCategory = true
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(text = "Add/delete Categories")
                }
            }
        }
        if (isCreatingNote) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
            {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    {
                        Card(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color.White)
                        ) {
                            // Show the note creation screen when isCreatingNote is true
                            NoteCreationScreen(
                                note = selectedNote,
                                onNoteCreated = { newNote ->

                                    val note = Note(
                                        title = newNote.title,
                                        content = newNote.content,
                                        categoryId = newNote.categoryId,
                                        reminderId = newNote.reminderId
                                    )

                                    GlobalScope.launch(Dispatchers.Main) { noteDao.insert(note) }
                                    selectedNote = null
                                    isCreatingNote = false
                                },
                                onNoteEdited = { editNote ->
                                    GlobalScope.launch(Dispatchers.Main) { noteDao.update(editNote) }

                                    selectedNote = null
                                    isCreatingNote = false
                                },
                                onCancel = {
                                    selectedNote = null
                                    isCreatingNote = false
                                },

                                onReminderCreate = {
                                    isCreatingReminder = true
                                    isCreatingNote = false
                                    isCreatingCategory = false
                                },
                                onDelete = {
                                    // handle deleting of the note here
                                    selectedNote = null
                                    isCreatingNote = false
                                },
                                categories = categoryDao.getAllCategories(),
                                reminderName = reminderDao.getReminderById(selectedNote?.reminderId).collectAsState(initial = "")
                            )
                        }
                    }
                }
            }
        }
        if (isCreatingCategory) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                CategorySelectionScreen(
                    categories = categoryDao.getAllCategories(),
                    onCategoryCreated = { categoryName ->
                        val category = Category(name = categoryName)
                        GlobalScope.launch(Dispatchers.Main) { categoryDao.insert(category) }
                    },
                    onCategoryDeleted = { categoryId ->
                        if (categoryId != null) {
                            GlobalScope.launch(Dispatchers.IO) { categoryDao.delete(categoryId) }
                        }
                    },
                    onCancel = {
                        isCreatingCategory = false
                        isCreatingNote = false
                    }
                )
            }
        }

        if (isCreatingReminder) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                ReminderSelectionScreen(
                    reminders = reminderDao.getAllReminders(),
                    selectedReminderId = selectedNote?.reminderId,
                    onReminderSelected = { reminderId ->
                        // Handle reminder selection and update the reminderId in the note
                        selectedNote?.reminderId = reminderId
                        isCreatingNote = true // Return to the NoteCreationScreen
                    },
                    onReminderCreated = { reminderDateTime ->
//                        val reminder = Reminder(dateTime = reminderDateTime, noteId = selectedNote.id)
//                        //println(categoryName)
//
//                        GlobalScope.launch (Dispatchers.Main) { reminderDao.insert(reminder) }
//
//                        selectedNote?.reminderId = reminder.id
                        isCreatingReminder = false
                        isCreatingNote = true
                    },
                    onCancel = {
                        isCreatingReminder = false
                        isCreatingNote = true
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun NoteCreationScreen(
    note: Note?, // nullable note parameter in-case we are opening a note instead of creating
    onNoteCreated: (Note) -> Unit,
    onNoteEdited: (Note) -> Unit,
    onCancel: () -> Unit,
    onReminderCreate: () -> Unit,
    onDelete: () -> Unit,
    categories: LiveData<List<Category>>,
    reminderName: State<String?>
) {
    // Define state variables for user input
    val categoriesList = categories.observeAsState(emptyList())

    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }
    var categoryId by remember { mutableStateOf<Long?>(null) }
    var reminderId by remember { mutableStateOf<Long?>(null) }
    var reminder by remember { mutableStateOf("")}

    reminder = reminderName.value ?: ""

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
        TextField(
            value = reminder,
            onValueChange = { reminder = it },
            label = { Text("Reminder date/time") },
            modifier = Modifier.fillMaxWidth()
        )


        var expandedCatDropdown by remember { mutableStateOf(false) }

        //This is bad lol but whatever
        //Set it to a empty string so we can have a non eye catching label
        //Wait for the for each below to update the categoriesList state
        //When we have a state we grab the first value
        var selectedCategoryName by remember { mutableStateOf("") }

        // If an existing category ID is provided, find and set the initial state
        if (note?.categoryId != null) {
            val existingCategory = categoriesList.find { it.id == note?.categoryId }
            if (existingCategory != null) {
                selectedCategoryName = existingCategory.name
                //categoryId = note?.categoryId
                println("category exists")
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp)
        ) {
            ExposedDropdownMenuBox(
                expanded = expandedCatDropdown,
                onExpandedChange = {
                    expandedCatDropdown = !expandedCatDropdown
                }
            ) {

                TextField(
                    value = selectedCategoryName,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCatDropdown) },
                    modifier = Modifier.fillMaxWidth()
                )


                DropdownMenu(
                    expanded = expandedCatDropdown,
                    onDismissRequest = { expandedCatDropdown = false }
                ) {
                    categoriesList.forEach { category ->
                        DropdownMenuItem(
                            onClick = {
                                selectedCategoryName = category.name
                                note?.categoryId = category.id
                                categoryId = category.id
                                expandedCatDropdown = false
                            }
                        ) {
                            Text(text = category.name)
                        }
                    }
                }
            }
        }

        // Reminder button (show only if it's an existing note)
        if (note != null) {
            // Button to select or create a reminder
            Button(
                onClick = { onReminderCreate() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Select/Create Reminder")
            }
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
                    if (note == null) {
                        val newNote = Note(
                            id = 0L,
                            title = title,
                            content = content,
                            categoryId = categoryId,
                            reminderId = reminderId
                        )
                        onNoteCreated(newNote)
                    } else {
                        note.title = title
                        note.content = content
                        //note.categoryId = categoryId
                        note.reminderId = reminderId
                        onNoteEdited(note)
                    }

                },
            ) {
                if (note == null) {
                    Text("Create")
                } else {
                    Text("Save")
                }

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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun CategorySelectionScreen(
    categories: LiveData<List<Category>>,
    onCategoryCreated: (String) -> Unit,
    onCategoryDeleted: (Long?) -> Unit,
    onCancel: () -> Unit
) {
    val categoriesList = categories.observeAsState(emptyList())
    var newCategoryName by remember { mutableStateOf("") }
    var isCreatingNewCategory by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }
    var selectedCategoryName by remember { mutableStateOf<String>("") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }

    var expandedCatDropdown by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                Text(
                    text = "Select Category",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp)
                ) {
                    ExposedDropdownMenuBox(
                        expanded = expandedCatDropdown,
                        onExpandedChange = {
                            expandedCatDropdown = !expandedCatDropdown
                        }
                    ) {

                        TextField(
                            value = selectedCategoryName,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCatDropdown) },
                            modifier = Modifier.fillMaxWidth()
                        )


                        DropdownMenu(
                            expanded = expandedCatDropdown,
                            onDismissRequest = { expandedCatDropdown = false }
                        ) {
                            categoriesList.forEach { category ->
                                DropdownMenuItem(
                                    onClick = {
                                        selectedCategoryName = category.name
                                        selectedCategoryId = category.id
                                        expandedCatDropdown = false
                                        isCreatingNewCategory = false
                                    }
                                ) {
                                    Text(text = category.name)
                                }
                            }
                        }
                    }
                }

//                Box(
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(
//                        "Pick a category to delete", modifier = Modifier
//                            .fillMaxWidth()
//                            .clickable(onClick = { expanded = true })
//                            .background(Color.LightGray)
//                    )
//
//
//                    // Dropdown to select an existing category
//                    DropdownMenu(
//                        expanded = expanded,
//                        onDismissRequest = { expanded = false }
//                    ) {
//                        categoriesList.forEach { category ->
//                            DropdownMenuItem(
//                                onClick = {
//                                    selectedCategoryName = category.name
//                                    selectedCategoryId = category.id
//                                    isCreatingNewCategory = false
//                                    expanded = false
//                                }
//                            ) {
//                                Text(text = category.name)
//                            }
//                        }
//                    }
//
//                }

                // Button to show text field for creating a new category
                Button(
                    onClick = { isCreatingNewCategory = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Create new Category")
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
                                    newCategoryName = ""
                                }
                            }
                        ) {
                            Text("Add")
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

                selectedCategoryName?.let { categoryName ->
                    Text("Category chosen: $categoryName", modifier = Modifier.padding(top = 16.dp))

                    Button(
                        onClick = {
                            selectedCategoryName = ""
                            onCategoryDeleted(selectedCategoryId)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp)
                    ) {
                        Text("Delete")
                    }

                }

//                Button(
//                    onClick = { onCategorySelected(selectedCategoryId) },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(top = 16.dp)
//                ) {
//                    Text("Apply")
//                }

                Button(
                    onClick = onCancel,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text("Cancel")
                }
            }
        }
    }
}

@Composable
fun ReminderSelectionScreen(
    reminders: LiveData<List<Reminder>>,
    selectedReminderId: Long?,
    onReminderSelected: (Long) -> Unit,
    onReminderCreated: (String) -> Unit,
    onCancel: () -> Unit
) {
    val remindersList = reminders.observeAsState(emptyList())
    var newReminderDateTime by remember { mutableStateOf("") }
    var isCreatingNewReminder by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "Select/Create Reminder",
                style = MaterialTheme.typography.h5,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                "Pick a reminder", modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = { expanded = true })
                    .background(
                        Color.Gray
                    )
            )
            // Dropdown to select an existing reminder
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                remindersList.forEach { reminder ->
                    DropdownMenuItem(
                        onClick = {
                            onReminderSelected(reminder.id)
                            isCreatingNewReminder = false
                        }
                    ) {
                        Text(text = reminder.dateTime)
                    }
                }
            }

            // Button to show text field for creating a new reminder
            Button(
                onClick = { isCreatingNewReminder = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Add New Reminder")
            }

            if (isCreatingNewReminder) {
                // Text field for entering a new reminder name
                TextField(
                    value = newReminderDateTime,
                    onValueChange = { newReminderDateTime = it },
                    label = { Text("Date/Time as a string") },
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
                            if (newReminderDateTime.isNotBlank()) {
                                onReminderCreated(newReminderDateTime)
                                isCreatingNewReminder = false
                                newReminderDateTime = ""
                            }
                        }
                    ) {
                        Text("Save")
                    }

                    Button(
                        onClick = {
                            isCreatingNewReminder = false
                            newReminderDateTime = ""
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
}