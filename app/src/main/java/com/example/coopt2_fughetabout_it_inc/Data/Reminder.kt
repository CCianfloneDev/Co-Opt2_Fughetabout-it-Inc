package com.example.coopt2_fughetabout_it_inc.Data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminders")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val dateTime: Long,
    val noteId: Long
)
