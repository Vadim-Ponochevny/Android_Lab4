package com.example.android_lab4.ui.note

import com.example.android_lab4.data.model.Note

sealed interface NoteEvent {
    object ShowDialog: NoteEvent
    object HideDialog: NoteEvent
    data class DeleteNote (val note: Note): NoteEvent
    data class EditNote (val note: Note): NoteEvent
    data class SaveNote(val title: String, val description: String) : NoteEvent
}