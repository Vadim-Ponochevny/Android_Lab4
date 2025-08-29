package com.example.android_lab4.domain.repository

import com.example.android_lab4.data.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun upsertNoteRepository(note: Note)

    suspend fun deleteNoteRepository(note: Note)

    fun getAllNotesRepository() : Flow<List<Note>>
}