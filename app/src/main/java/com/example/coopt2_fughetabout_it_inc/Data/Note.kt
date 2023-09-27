package com.example.coopt2_fughetabout_it_inc.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val content: String,
    val categoryId: Long,
    val reminderId: Long
)
