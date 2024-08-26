package com.example.passwordManagement

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_password_generator.*
import kotlinx.android.synthetic.main.activity_password_strength_calculator.*
import kotlinx.android.synthetic.main.activity_reset_password.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class PasswordStrengthCalculator : AppCompatActivity() {

    private var color: Int = R.color.weak

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_strength_calculator)

        val passwordStrengthCalculatorLogic = PasswordStrengthCalculatorLogic()
        passwordInput.addTextChangedListener(passwordStrengthCalculatorLogic)

        passwordStrengthCalculatorLogic.strengthLevel.observe(
            this,
            Observer { strengthLevel -> displayStrengthLevel(strengthLevel) })
        passwordStrengthCalculatorLogic.strengthColor.observe(
            this,
            Observer { strengthColor -> color = strengthColor })

        passwordStrengthCalculatorLogic.lowerCase.observe(
            this,
            Observer { value -> displayPasswordSuggestions(value, imgLowerCase, txtLowerCase) })
        passwordStrengthCalculatorLogic.upperCase.observe(
            this,
            Observer { value -> displayPasswordSuggestions(value, imgUpperCase, txtUpperCase) })
        passwordStrengthCalculatorLogic.number.observe(
            this,
            Observer { value -> displayPasswordSuggestions(value, imgNumber, txtNumber) })
        passwordStrengthCalculatorLogic.specialChar.observe(
            this,
            Observer { value -> displayPasswordSuggestions(value, imgSpecialChar, txtSpecialChar) })

        imgBack4.setOnClickListener {
            super.onBackPressed()
        }

        saveButton.setOnClickListener {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clip = ClipData.newPlainText("cSave", passwordInput.text)
            clipboard.setPrimaryClip(clip)
            Toast.makeText(this@PasswordStrengthCalculator, "Copy to Clipboard", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayPasswordSuggestions(value: Int, imageView: ImageView, textView: TextView) {
        if (value == 1) {
            imageView.setColorFilter(ContextCompat.getColor(this, R.color.veryStrong))
            textView.setTextColor(ContextCompat.getColor(this, R.color.veryStrong))
        } else {
            imageView.setColorFilter(ContextCompat.getColor(this, R.color.ColorWhite))
            textView.setTextColor(ContextCompat.getColor(this, R.color.ColorWhite))
        }
    }

    private fun displayStrengthLevel(strengthLevel: StrengthLevel) {
        txtStrengthLevel.text = strengthLevel.name
        txtStrengthLevel.setTextColor(ContextCompat.getColor(this, color))
        strengthLevelIndicator.setBackgroundColor(ContextCompat.getColor(this, color))
    }
}

enum class StrengthLevel {
    WEAK,
    MEDIUM,
    STRONG,
    EXCELLENT
}

class PasswordStrengthCalculatorLogic : TextWatcher {

    var strengthLevel: MutableLiveData<StrengthLevel> = MutableLiveData()
    var strengthColor: MutableLiveData<Int> = MutableLiveData()

    val lowerCase = MutableLiveData<Int>().apply { postValue(0)}
    val upperCase = MutableLiveData<Int>().apply { postValue(0)}
    val number = MutableLiveData<Int>().apply { postValue(0)}
    val specialChar = MutableLiveData<Int>().apply { postValue(0)}

    override fun afterTextChanged(p0: Editable?) {}
    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
    override fun onTextChanged(char: CharSequence?, p1: Int, p2: Int, p3: Int) {
        if(char != null){
            lowerCase.value = if (char.hasLowerCase()) { 1 } else { 0 }
            upperCase.value = if (char.hasUpperCase()) { 1 } else { 0 }
            number.value = if (char.hasDigit()) { 1 } else { 0 }
            specialChar.value = if (char.hasSpecialChar()) { 1 } else { 0 }
            calculateStrength(char)
        }
    }

    private fun calculateStrength(password: CharSequence) {
        if(password.length in 0..7){
            strengthColor.value = R.color.weak
            strengthLevel.value = StrengthLevel.WEAK
        }else if(password.length in 8..10){
            if(lowerCase.value == 1 || upperCase.value == 1 || number.value == 1 || specialChar.value == 1){
                strengthColor.value = R.color.medium
                strengthLevel.value = StrengthLevel.MEDIUM
            }
        }else if(password.length in 11..16){
            if(lowerCase.value == 1 || upperCase.value == 1 || number.value == 1 || specialChar.value == 1){
                if(lowerCase.value == 1 && upperCase.value == 1){
                    strengthColor.value = R.color.strong
                    strengthLevel.value = StrengthLevel.STRONG
                }
            }
        }else if(password.length > 16){
            if(lowerCase.value == 1 && upperCase.value == 1 && number.value == 1 && specialChar.value == 1){
                strengthColor.value = R.color.veryStrong
                strengthLevel.value = StrengthLevel.EXCELLENT
            }
        }
    }

    private fun CharSequence.hasLowerCase(): Boolean{
        val pattern: Pattern = Pattern.compile("[a-z]")
        val hasLowerCase: Matcher = pattern.matcher(this)
        return hasLowerCase.find()
    }

    private fun CharSequence.hasUpperCase(): Boolean{
        val pattern: Pattern = Pattern.compile("[A-Z]")
        val hasUpperCase: Matcher = pattern.matcher(this)
        return hasUpperCase.find()
    }

    private fun CharSequence.hasDigit(): Boolean{
        val pattern: Pattern = Pattern.compile("[0-9]")
        val hasDigit: Matcher = pattern.matcher(this)
        return hasDigit.find()
    }

    private fun CharSequence.hasSpecialChar(): Boolean{
        val pattern: Pattern = Pattern.compile("[!@#$%^&*()_=+{}/?',.<>|\\[\\]~-]")
        val hasSpecialChar: Matcher = pattern.matcher(this)
        return hasSpecialChar.find()
    }
}