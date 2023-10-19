package com.example.coopt2_fughetabout_it_inc.data

import androidx.lifecycle.ViewModel

/**
 * ViewModel for managing notes data.
 *
 * @property noteDao The data access object for notes.
 */
class NotesViewModel(
    private val noteDao: NoteDao
) : ViewModel()

/**
 * ViewModel for managing categories data.
 *
 * @property categoryDao The data access object for categories.
 */
class CategoriesViewModel(
    private val categoryDao: CategoryDao
) : ViewModel()
