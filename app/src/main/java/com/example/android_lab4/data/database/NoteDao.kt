package com.example.android_lab4.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.android_lab4.data.model.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NoteDao {
    @Upsert
    suspend fun upsertNoteDatabase(note: Note)

    @Delete
    suspend fun deleteNoteDatabase(note: Note)

    @Query("SELECT * FROM note")
    fun getAllNotesDatabase() : Flow<List<Note>>
}