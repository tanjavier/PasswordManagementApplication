package com.example.passwordManagement

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.room.Room
import com.example.passwordManagement.database.KeyDatabase
import kotlinx.android.synthetic.main.fragment_create_password.view.*
import kotlinx.android.synthetic.main.fragment_password_key.*
import kotlinx.android.synthetic.main.fragment_password_key.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PasswordKey : DialogFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val db : KeyDatabase = Room.databaseBuilder(requireContext().applicationContext, KeyDatabase::class.java, "key").build()
        val rootView: View = inflater.inflate(R.layout.fragment_password_key, container, false)

        GlobalScope.launch {
            val key = db.KeyDao().getSpecificKey(1)

            rootView.enterButton.setOnClickListener {
                when {
                    txtKey.text.toString() == key.toString() -> {
                        startActivity(Intent(context, HomeMenu::class.java))
                        activity?.finish()
                    }
                    txtKey.text.toString() != key.toString() -> {
                        txtKey.error = "Incorrect Password"
                    }
                    txtKey.text.toString().isEmpty() -> {
                        txtKey.error = "Password Key is empty"
                    }
                }
            }
        }

        rootView.cancelButton.setOnClickListener {
            dismiss()
        }

        return rootView
    }
}