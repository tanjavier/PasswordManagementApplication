package com.example.passwordManagement

import android.os.Build.ID
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.example.passwordManagement.database.KeyDatabase
import com.example.passwordManagement.entities.Key
import kotlinx.android.synthetic.main.activity_reset_password.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ResetPassword : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        val db: KeyDatabase = Room.databaseBuilder(applicationContext, KeyDatabase::class.java, "key").build()
        val resetButton = findViewById<Button>(R.id.resetButton)

        resetButton.setOnClickListener {
            var passwordInput1 = findViewById<EditText>(R.id.passwordInput1).text.toString()
            var passwordInput2 = findViewById<EditText>(R.id.passwordInput2).text.toString()
            var passwordInput3 = findViewById<EditText>(R.id.passwordInput3).text.toString()

            GlobalScope.launch {
                val KEY = db.KeyDao().getSpecificKey(1)

                if (passwordInput1 == KEY.toString() && passwordInput2 == passwordInput3 && passwordInput2.isNotEmpty()) {
                    val key = Key(passwordInput2)
                    key.id = 1
                    db.KeyDao().updateKey(key)
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@ResetPassword, "Password Key has been changed!", Toast.LENGTH_SHORT).show()
                    }
                } else if (passwordInput1 != KEY.toString()) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@ResetPassword, "Current Password Key is Incorrect!", Toast.LENGTH_SHORT).show()
                    }
                } else if (passwordInput2 != passwordInput3) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@ResetPassword, "New Password Keys do not match!", Toast.LENGTH_SHORT).show()
                    }
                } else if (passwordInput1.isNullOrEmpty() || passwordInput2.isNullOrEmpty() || passwordInput3.isNullOrEmpty()) {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(this@ResetPassword, "Please fill in the empty areas!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        imgBack1.setOnClickListener {
            super.onBackPressed()
        }
    }
}