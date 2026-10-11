package com.example.datapersistencedemo

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "StudentDB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(
            "CREATE TABLE students (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name TEXT, " +
                    "course TEXT)"
        )
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {

        db.execSQL("DROP TABLE IF EXISTS students")
        onCreate(db)
    }

    fun insertStudent(name: String, course: String): Boolean {

        val db = writableDatabase

        val values = ContentValues()

        values.put("name", name)
        values.put("course", course)

        val result = db.insert("students", null, values)

        db.close()

        return result != -1L
    }

    fun getStudents(): String {

        val db = readableDatabase

        val cursor = db.rawQuery(
            "SELECT * FROM students",
            null
        )

        val result = StringBuilder()

        while (cursor.moveToNext()) {

            val id = cursor.getInt(0)
            val name = cursor.getString(1)
            val course = cursor.getString(2)

            result.append("$id. $name - $course\n")
        }

        cursor.close()
        db.close()

        return result.toString()
    }
}