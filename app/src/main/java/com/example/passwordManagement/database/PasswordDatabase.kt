package com.example.passwordManagement.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.passwordManagement.dao.PasswordDao
import com.example.passwordManagement.entities.Password

@Database(entities = [Password::class], version = 1, exportSchema = false)
abstract class PasswordDatabase : RoomDatabase() {

    companion object {
        var passwordDatabase: PasswordDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): PasswordDatabase {
            if (passwordDatabase == null) {
                passwordDatabase = Room.databaseBuilder(context, PasswordDatabase::class.java, "passwords.db").build()
            }
            return passwordDatabase!!
        }
    }

    abstract fun PasswordDao(): PasswordDao
}