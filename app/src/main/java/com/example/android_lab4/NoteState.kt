package com.example.android_lab4

data class NoteState(
    val notes: List<Note> = emptyList(),
    val title: String = "",
    val description: String = "",
    val isAddingNote: Boolean = false,
)
