package com.example.android_lab4.ui.note

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.android_lab4.data.model.Note
import com.example.android_lab4.domain.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
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
            NoteEvent.ShowDialog -> {
                _state.update { it.copy(
                    dialogIsOpen = true
                ) }
            }

            NoteEvent.HideDialog -> {
                _state.update { it.copy(
                    dialogIsOpen = false,
                    editing = false,
                    title = "",
                    description = "",
                    editingNoteId = null
                ) }
            }

            is NoteEvent.DeleteNote -> {
                viewModelScope.launch{
                    repository.deleteNoteRepository(event.note)
                }
            }

            is NoteEvent.SaveNote -> {
                val title = event.title
                val description = event.description
                val id = _state.value.editingNoteId

                if (title.isBlank() || description.isBlank()) {
                    Log.d("SAVE_NOTE", "Title or description is blank")
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

                _state.update { it.copy(
                    dialogIsOpen = false,
                    editing = false,
                    title = "",
                    description = "",
                    editingNoteId = null
                ) }

            }
            is NoteEvent.EditNote -> {
                _state.update { it.copy(
                    editing = true,
                    title = event.note.title,
                    description = event.note.description,
                    editingNoteId = event.note.id
                )}
            }
        }
    }
}