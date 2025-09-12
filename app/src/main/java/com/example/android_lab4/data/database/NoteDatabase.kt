package com.example.android_lab4.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.android_lab4.data.model.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDatabase() : RoomDatabase() {
    companion object {
        const val DATABASE_NAME = "notes_table"
    }
    abstract val dao: NoteDao
}