package com.example.passwordManagement

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeMenu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_menu)

        val option1 = findViewById<TextView>(R.id.op1)
        val option2 = findViewById<TextView>(R.id.op2)
        val option3 = findViewById<TextView>(R.id.op3)
        val option4 = findViewById<TextView>(R.id.op4)

        option1.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        option2.setOnClickListener {
            val intent = Intent(this, PasswordGenerator::class.java)
            startActivity(intent)
        }

        option3.setOnClickListener {
            val intent = Intent(this, PasswordStrengthCalculator::class.java)
            startActivity(intent)
        }

        option4.setOnClickListener {
            val intent = Intent(this, ResetPassword::class.java)
            startActivity(intent)
        }
    }
}



