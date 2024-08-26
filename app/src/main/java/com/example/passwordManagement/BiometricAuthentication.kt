package com.example.passwordManagement

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.room.Room
import com.example.passwordManagement.database.KeyDatabase
import kotlinx.android.synthetic.main.activity_biometric_authentication.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class BiometricAuthentication : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_biometric_authentication)

        val isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE).getBoolean("isFirstRun", true)

        if (isFirstRun) {
            startActivity(Intent(this@BiometricAuthentication, FirstTimeUser::class.java))
            Toast.makeText(this@BiometricAuthentication, "First Time User", Toast.LENGTH_LONG).show()
        }

        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit().putBoolean("isFirstRun", false).apply()

        btnPassword.setOnClickListener {
            var dialog = PasswordKey()
            dialog.show(supportFragmentManager, "customDialog")
        }

        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Toast.makeText(this@BiometricAuthentication, "Authentication error: $errString", Toast.LENGTH_SHORT).show()
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)
                Toast.makeText(this@BiometricAuthentication, "Authentication succeeded!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@BiometricAuthentication, HomeMenu::class.java))
                val openMainAct = Intent(this@BiometricAuthentication, HomeMenu::class.java)
                startActivity(openMainAct)
                finish()
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Toast.makeText(this@BiometricAuthentication, "Authentication failed", Toast.LENGTH_SHORT).show()
            }
        })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Enter using your biometric credential")
            .setNegativeButtonText("Cancel")
            .build()

        btnFinger.setOnClickListener{
            biometricPrompt.authenticate(promptInfo)
        }
    }
}