package com.example.passwordManagement.dao

import androidx.room.*
import com.example.passwordManagement.entities.Password

@Dao
interface PasswordDao {

    @Query("SELECT * FROM password ORDER BY id DESC")
    suspend fun getAllPasswords() : List<Password>

    @Query("SELECT * FROM password WHERE id =:id")
    suspend fun getSpecificPassword(id:Int) : Password

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassword(note: Password)

    @Delete
    suspend fun deletePassword(note: Password)

    @Query("DELETE FROM password WHERE id =:id")
    suspend fun deleteSpecificPassword(id:Int)

    @Update
    suspend fun updatePassword(note: Password)
}