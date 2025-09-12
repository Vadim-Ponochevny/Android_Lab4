package com.example.android_lab4.data.repository

import com.example.android_lab4.data.database.NoteDao
import com.example.android_lab4.data.model.Note
import com.example.android_lab4.domain.repository.NoteRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class NoteRepositoryImpl @Inject constructor(private val noteDao: NoteDao) : NoteRepository{
    override suspend fun deleteNoteRepository(note: Note) {
        return noteDao.deleteNoteDatabase(note)
    }

    override fun getAllNotesRepository(): Flow<List<Note>> {
        return noteDao.getAllNotesDatabase()
    }

    override suspend fun upsertNoteRepository(note: Note) {
        return noteDao.upsertNoteDatabase(note)
    }
}