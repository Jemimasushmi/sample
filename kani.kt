package com.example.kanishkasql
import DatabaseHelper
import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.kanishkasql.R

class MainActivity : AppCompatActivity() {
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var database: SQLiteDatabase
    private lateinit var nameInput: EditText
    private lateinit var ageInput: EditText
    private lateinit var addButton: Button
    private lateinit var updateButton: Button
    private lateinit var deleteButton: Button
    private lateinit var viewButton: Button
    private lateinit var resultText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        dbHelper = DatabaseHelper(this)
        database = dbHelper.writableDatabase
        nameInput = findViewById(R.id.nameInput)
        ageInput = findViewById(R.id.ageInput)
        addButton = findViewById(R.id.addButton)
        updateButton = findViewById(R.id.updateButton)
        deleteButton = findViewById(R.id.deleteButton)
        viewButton = findViewById(R.id.viewButton)
        resultText = findViewById(R.id.resultText)
        addButton.setOnClickListener { addData() }
        updateButton.setOnClickListener { updateData() }
        deleteButton.setOnClickListener { deleteData() }
        viewButton.setOnClickListener { viewData() }
    }
    private fun addData() {
        val name = nameInput.text.toString()
        val age = ageInput.text.toString().toIntOrNull()
        if (name.isNotEmpty() && age != null) {
            val values = ContentValues().apply {
                put("name", name)
                put("age", age)
            }
            database.insert("users", null, values)
            Toast.makeText(this, "Data Inserted",
                Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Please enter valid details",
                Toast.LENGTH_SHORT).show()
        }
    }
    private fun updateData() {
        val name = nameInput.text.toString()
        val age = ageInput.text.toString().toIntOrNull()
        if (name.isNotEmpty() && age != null) {
            val values = ContentValues().apply {
                put("age", age)
            }
            val rowsUpdated = database.update("users", values, "name=?",
                arrayOf(name))
            if (rowsUpdated > 0) {
                Toast.makeText(this, "Data Updated",
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No record found",
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter valid details",
                Toast.LENGTH_SHORT).show()
        }
    }
    private fun deleteData() {
        val name = nameInput.text.toString()
        if (name.isNotEmpty()) {
            val rowsDeleted = database.delete("users", "name=?",
                arrayOf(name))
            if (rowsDeleted > 0) {
                Toast.makeText(this, "Data Deleted",
                    Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No record found",
                    Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Please enter a name",
                Toast.LENGTH_SHORT).show()
        }
    }
    private fun viewData() {
        val cursor: Cursor = database.rawQuery("SELECT * FROM users", null)
        val data = StringBuilder()
        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(0)
                val name = cursor.getString(1)
                val age = cursor.getInt(2)
                data.append("ID: $id, Name: $name, Age: $age\n")
            } while (cursor.moveToNext())
        } else {
            data.append("No records found.")
        }
        cursor.close()
        resultText.text = data.toString()
    }
}