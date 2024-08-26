package com.example.passwordManagement

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.passwordManagement.database.KeyDatabase
import com.example.passwordManagement.entities.Key
import kotlinx.android.synthetic.main.activity_first_time_user.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class FirstTimeUser : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first_time_user)

        var db : KeyDatabase = Room.databaseBuilder(applicationContext, KeyDatabase::class.java, "key").build()
        val confirmButton = findViewById<Button>(R.id.confirmButton)

        confirmButton.setOnClickListener {
            var keyInput1 = findViewById<EditText>(R.id.keyInput1).text.toString()
            var keyInput2 = findViewById<EditText>(R.id.keyInput2).text.toString()

            if (keyInput1 == keyInput2 && keyInput1.isNotEmpty()) {
                var key = Key(keyInput1)
                GlobalScope.launch {
                    db.KeyDao().insertKey(key)
                }
                startActivity(Intent(this@FirstTimeUser, BiometricAuthentication::class.java))
            } else if (keyInput1 != keyInput2) {
                Toast.makeText(this@FirstTimeUser, "Password Keys do not match!", Toast.LENGTH_SHORT).show()
            } else if (keyInput1.isEmpty()  || keyInput1 == "") {
                Toast.makeText(this@FirstTimeUser, "Please enter Password Keys!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}