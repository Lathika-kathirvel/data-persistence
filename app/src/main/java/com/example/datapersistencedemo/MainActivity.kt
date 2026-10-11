package com.example.datapersistencedemo

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var btnSaveName: Button
    private lateinit var tvSavedName: TextView

    private lateinit var etStudentName: EditText
    private lateinit var etCourse: EditText
    private lateinit var btnAddStudent: Button
    private lateinit var tvStudents: TextView

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        // SharedPreferences views
        etName = findViewById(R.id.etName)
        btnSaveName = findViewById(R.id.btnSaveName)
        tvSavedName = findViewById(R.id.tvSavedName)

        // SQLite views
        etStudentName = findViewById(R.id.etStudentName)
        etCourse = findViewById(R.id.etCourse)
        btnAddStudent = findViewById(R.id.btnAddStudent)
        tvStudents = findViewById(R.id.tvStudents)

        // Create database
        databaseHelper = DatabaseHelper(this)

        // Load saved SharedPreferences data
        loadSavedName()

        // Display existing SQLite records
        displayStudents()

        // Save name using SharedPreferences
        btnSaveName.setOnClickListener {

            val name = etName.text.toString()

            if (name.isNotEmpty()) {

                val preferences = getSharedPreferences(
                    "MyPreferences",
                    Context.MODE_PRIVATE
                )

                val editor = preferences.edit()

                editor.putString("student_name", name)

                editor.apply()

                tvSavedName.text = "Saved Name: $name"

                Toast.makeText(
                    this,
                    "Name saved successfully",
                    Toast.LENGTH_SHORT
                ).show()

            } else {

                Toast.makeText(
                    this,
                    "Please enter your name",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        // Add student to SQLite
        btnAddStudent.setOnClickListener {

            val studentName = etStudentName.text.toString()
            val course = etCourse.text.toString()

            if (studentName.isNotEmpty() && course.isNotEmpty()) {

                val inserted = databaseHelper.insertStudent(
                    studentName,
                    course
                )

                if (inserted) {

                    Toast.makeText(
                        this,
                        "Student added successfully",
                        Toast.LENGTH_SHORT
                    ).show()

                    etStudentName.text.clear()
                    etCourse.text.clear()

                    displayStudents()

                } else {

                    Toast.makeText(
                        this,
                        "Failed to add student",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {

                Toast.makeText(
                    this,
                    "Please enter all details",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun loadSavedName() {

        val preferences = getSharedPreferences(
            "MyPreferences",
            Context.MODE_PRIVATE
        )

        val savedName = preferences.getString(
            "student_name",
            ""
        )

        if (!savedName.isNullOrEmpty()) {

            tvSavedName.text = "Saved Name: $savedName"
        }
    }

    private fun displayStudents() {

        val students = databaseHelper.getStudents()

        if (students.isEmpty()) {

            tvStudents.text = "Students:\nNo records found"

        } else {

            tvStudents.text = "Students:\n$students"
        }
    }
}