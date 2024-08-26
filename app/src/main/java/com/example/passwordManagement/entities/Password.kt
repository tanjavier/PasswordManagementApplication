package com.example.passwordManagement.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "Password")
class Password: Serializable {

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null

    @ColumnInfo(name = "service")
    var service:String? = null

    @ColumnInfo(name = "username_email")
    var usernameEmail:String? = null

    @ColumnInfo(name = "date_time")
    var dateTime:String? = null

    @ColumnInfo(name = "password_text")
    var passwordText:String? = null

    @ColumnInfo(name = "web_link")
    var webLink:String? = null

    @ColumnInfo(name = "color")
    var color:String? = null


    override fun toString(): String {

        return "$service : $dateTime"

    }
}