package com.example.passwordManagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.passwordManagement.dao.KeyDao
import com.example.passwordManagement.entities.Key

@Database(entities = [Key::class], version = 1, exportSchema = false)
abstract class KeyDatabase : RoomDatabase() {

    companion object {
        var keyDatabase: KeyDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): KeyDatabase {
            if (keyDatabase == null) {
                keyDatabase = Room.databaseBuilder(context, KeyDatabase::class.java, "key.db").build()
            }
            return keyDatabase!!
        }
    }

    abstract fun KeyDao(): KeyDao
}