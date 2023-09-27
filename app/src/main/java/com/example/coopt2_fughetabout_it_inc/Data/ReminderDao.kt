package com.example.coopt2_fughetabout_it_inc.Data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ReminderDao {
    @Query("SELECT * FROM reminders")
    fun getAllReminders(): LiveData<List<Reminder>>

    @Insert
    suspend fun insert(reminder: Reminder)
}
