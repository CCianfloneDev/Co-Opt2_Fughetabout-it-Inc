package com.example.coopt2_fughetabout_it_inc.Data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CategoryDao {
    @Query("SELECT * FROM categories")
    fun getAllCategories(): LiveData<List<Category>>

    @Insert
    suspend fun insert(category: Category)
}
