package com.example.android_lab4.ui.note

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_lab4.data.model.Note
import com.example.android_lab4.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository,
): ViewModel() {

    private val _state = MutableStateFlow(NoteState())
    val state: StateFlow<NoteState> = _state

    private val _notes = MutableStateFlow<List<Note>>(emptyList())
    val notes: StateFlow<List<Note>> = _notes

    init {
        getAllNotes()
    }

    private fun getAllNotes() {
        viewModelScope.launch {
            try {
                repository.getAllNotesRepository().collect { notes ->
                    _notes.value = notes
                }
            }
            catch (e: Exception) {
                _state.update { it.copy(
                    error = e.toString()
                ) }
            }
        }
    }

    fun onEvent(event: NoteEvent) {
        when(event) {
            is NoteEvent.ShowDialog -> openDialog()

            is NoteEvent.HideDialog -> closeDialog()

            is NoteEvent.DeleteNote -> deleteNote(event.note)

            is NoteEvent.SaveNote -> saveNote(event.title, event.description)

            is NoteEvent.EditNote -> editNote(event.note)
        }
    }

    private fun openDialog() {
        _state.update { it.copy(
            dialogIsOpen = true
        ) }
    }

    private fun closeDialog() {
        _state.update { it.copy(
            dialogIsOpen = false,
            editing = false,
            title = "",
            description = "",
            editingNoteId = null
        ) }
    }

    private fun deleteNote(note: Note) {
        viewModelScope.launch{
            repository.deleteNoteRepository(note)
        }
    }

    private fun saveNote(title: String, description: String) {
        // id при редактировании кладем и получаем из _state,
        // чтобы не таскать его лишний раз из диалога,
        // а внесенные пользователем title и description берем и event
        val id = _state.value.editingNoteId

        if (title.isBlank() || description.isBlank()) {
            return
        }

        val note = if (id == null) {
            Note(
                title = title,
                description = description
            )
        } else {
            Note(
                id = id,
                title = title,
                description = description
            )
        }

        viewModelScope.launch {
            repository.upsertNoteRepository(note)
            Log.d("SAVE_NOTE", "Note saved: $note")
        }

        closeDialog()
    }

    private fun editNote(note: Note) {
        _state.update { it.copy(
            editing = true,
            title = note.title,
            description = note.description,
            editingNoteId = note.id
        )}
    }
}