package com.example.passwordManagement

import android.R.attr
import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_password_generator.*
import android.content.ClipboardManager
import java.util.concurrent.ThreadLocalRandom
import android.R.attr.label

import android.content.ClipData
import android.content.Context
import android.widget.Toast


class PasswordGenerator : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_generator)

        var seekbar = findViewById<SeekBar>(R.id.seekbar)
        var length = findViewById<TextView>(R.id.length)

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                length.text = "Length : $progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        btnGenerate.setOnClickListener {
            val password = process(seekbar.progress)
            generated_password.text = password
        }

        imgBack3.setOnClickListener {
            super.onBackPressed()
        }

        btnSave.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("gSave", generated_password.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this@PasswordGenerator, "Copy to Clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        private const val LENGTH_MIN = 33
        private const val LENGTH_MAX = 126
        fun process(length: Int): String {
            val builder = StringBuilder()
            for (i in 0 until length) {
                builder.append(
                    ThreadLocalRandom.current().nextInt(LENGTH_MIN, LENGTH_MAX + 1).toChar()
                )
            }
            return builder.toString()
        }
    }
}