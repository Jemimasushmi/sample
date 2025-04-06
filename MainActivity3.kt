package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity3 : AppCompatActivity() {

    private lateinit var editName: EditText
    private lateinit var editEmail: EditText
    private lateinit var editPhone: EditText
    private lateinit var button: Button
    private lateinit var textView: TextView

    private val pref = "user_pref"
    private val keyName = "name"
    private val keyEmail = "email"
    private val keyPhone = "phone"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main3)

        editName = findViewById(R.id.editName)
        editEmail = findViewById(R.id.editEmail)
        editPhone = findViewById(R.id.editPhone)
        button = findViewById(R.id.button)
        textView = findViewById(R.id.textView2)

        val sharedPreferences = getSharedPreferences(pref, Context.MODE_PRIVATE)

        // Display saved values
        val savedName = sharedPreferences.getString(keyName, "No name")
        val savedEmail = sharedPreferences.getString(keyEmail, "No email")
        val savedPhone = sharedPreferences.getString(keyPhone, "No phone")

        textView.text = "Saved:\nName: $savedName\nEmail: $savedEmail\nPhone: $savedPhone"

        // Save values on button click
        button.setOnClickListener {
            val name = editName.text.toString()
            val email = editEmail.text.toString()
            val phone = editPhone.text.toString()

            sharedPreferences.edit()
                .putString(keyName, name)
                .putString(keyEmail, email)
                .putString(keyPhone, phone)
                .apply()

            textView.text = "Saved:\nName: $name\nEmail: $email\nPhone: $phone"
        }
    }
}
