package com.example.android_lab4

import com.example.android_lab4.data.model.Note

sealed interface NoteEvent {
    object SaveNote: NoteEvent
    data class SetTitle(val title: String) : NoteEvent
    data class SetDescription(val description: String) : NoteEvent
    object ShowDialog: NoteEvent
    object HideDialog: NoteEvent
    data class DeleteNote (val note: Note): NoteEvent
}