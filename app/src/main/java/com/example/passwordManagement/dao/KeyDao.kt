package com.example.passwordManagement.dao

import androidx.room.*
import com.example.passwordManagement.entities.Key
import com.example.passwordManagement.entities.Password

@Dao
interface KeyDao {
    @Query("SELECT * FROM password_key ORDER BY id DESC")
    suspend fun getAllKey() : List<Key>

    @Query("SELECT `key` FROM password_key WHERE id =:id")
    suspend fun getSpecificKey(id:Int) : Key

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertKey(note: Key)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateKey(note: Key)
}