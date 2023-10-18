package com.example.coopt2_fughetabout_it_inc.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NotesViewModel(
    private val noteDao: NoteDao
) : ViewModel() {

    val allNotes: LiveData<List<Note>> = noteDao.getAllNotes()
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
class CategoriesViewModel(
    private val categoryDao: CategoryDao
) : ViewModel() {

    val allCategories: LiveData<List<Category>> = categoryDao.getAllCategories()

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