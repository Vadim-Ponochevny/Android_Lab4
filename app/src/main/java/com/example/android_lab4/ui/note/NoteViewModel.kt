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
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository,
): ViewModel() {

    private val _state = MutableStateFlow(NoteState())

    private val _notes = repository.getAllNotesRepository()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _notes) { state, notes ->
        Log.d("STATE_DEBUG", "Notes from DB: $notes")
        state.copy(
            notes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), NoteState())

    fun onEvent(event: NoteEvent) {
        when(event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch{
                    repository.deleteNoteRepository(event.note)
                }
            }
            NoteEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingNote = false,
                    editingNoteId = null
                ) }
            }
            NoteEvent.SaveNote -> {
                val title = _state.value.title
                val description = _state.value.description
                val id = _state.value.editingNoteId ?: 0

                if (title.isBlank() || description.isBlank()) {
                    Log.d("SAVE_NOTE", "Title or description is blank")
                    return
                }

                val note = Note(
                    id = id,
                    title = title,
                    description = description
                )

                viewModelScope.launch {
                    repository.upsertNoteRepository(note)
                    Log.d("SAVE_NOTE", "Note saved: $note")
                }

                _state.update { it.copy(
                    isAddingNote = false,
                    title = "",
                    description = "",
                    editingNoteId = null
                ) }

            }
            is NoteEvent.SetTitle -> {
                _state.update { it.copy(
                    title = event.title
                ) }
                Log.d("TITLE_UPDATE_IN_STATE", _state.value.title)
            }
            is NoteEvent.SetDescription -> {
                _state.update { it.copy(
                    description = event.description
                ) }
            }
            NoteEvent.ShowDialog -> {
                _state.update { it.copy(
                    isAddingNote = true
                ) }
            }
            is NoteEvent.EditNote -> {
                _state.update { it.copy(
                    isAddingNote = true,
                    title = event.note.title,
                    description = event.note.description,
                    editingNoteId = event.note.id
                )}
            }
        }
    }
}