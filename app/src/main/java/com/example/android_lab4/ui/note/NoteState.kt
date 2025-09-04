package com.example.android_lab4.ui.note

import com.example.android_lab4.data.model.Note

data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: String = "",
    val description: String = "",
    val isAddingNote: Boolean = false,
    val editingNoteId: Int? = null
)
