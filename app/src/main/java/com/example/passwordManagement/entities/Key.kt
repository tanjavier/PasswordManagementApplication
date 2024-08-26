package com.example.passwordManagement.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "password_key")
data class Key(
    @ColumnInfo(name = "key")
    var key:String? ): Serializable {

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null

    override fun toString(): String {
        return "$key"
    }
}
