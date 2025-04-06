package com.example.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val button=findViewById<Button>(R.id.ebutton)
        button.setOnClickListener{
            val explicit= Intent(this,MainActivity2::class.java)
            startActivity(explicit)
        }

        val button2=findViewById<Button>(R.id.implicitbutton)
        button2.setOnClickListener{
       val button2=Intent(Intent.ACTION_VIEW)
            button2.data= Uri.parse("https://www.google.com/")
            startActivity(button2)
        }
    val button3=findViewById<Button>(R.id.button3)
        button3.setOnClickListener{
            val intent= Intent(this,MainActivity3::class.java)
            startActivity(intent)
        }

    }
}