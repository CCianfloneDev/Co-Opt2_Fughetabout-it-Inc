package com.example.coopt2_fughetabout_it_inc.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * ViewModel for managing notes data.
 *
 * @property noteDao The data access object for notes.
 */
class NotesViewModel(
    private val noteDao: NoteDao
) : ViewModel() {

    /**
     * LiveData representing a list of all notes.
     */
    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()

    /**
     * Inserts or updates a note in the database based on its ID.
     *
     * @param note The note to be inserted or updated.
     */
    fun insertOrUpdate(
        note: Note
    ) {
        viewModelScope.launch {
            if (note.id == 0L) {
                noteDao.insert(note)
            } else {
                noteDao.update(note)
            }
        }
    }
}

/**
 * ViewModel for managing categories data.
 *
 * @property categoryDao The data access object for categories.
 */
class CategoriesViewModel(
    private val categoryDao: CategoryDao
) : ViewModel() {

    /**
     * LiveData representing a list of all categories.
     */
    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()

    /**
     * Inserts or updates a category in the database based on its ID.
     *
     * @param category The category to be inserted or updated.
     */
     fun insertOrUpdate(category: Category) {
        viewModelScope.launch {
            if (category.id == 0L) {
                categoryDao.insert(category)
            } else {
                categoryDao.update(category)
            }
        }
    }
}