package com.example.android_lab4

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class NoteViewModel(
    private val dao: NoteDao,
): ViewModel() {

    private val _state = MutableStateFlow(NoteState())

    private val _notes = dao.getAllNotes()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    val state = combine(_state, _notes) { state, notes ->
        state.copy(
            notes = notes
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteState())


    fun onEvent(event: NoteEvent) {
        when(event) {
            is NoteEvent.DeleteNote -> {
                viewModelScope.launch{
                    dao.deleteNote(event.note)
                }
            }
            NoteEvent.HideDialog -> {
                _state.update { it.copy(
                    isAddingNote = false
                ) }
            }
            NoteEvent.SaveNote -> {
                val title = state.value.title
                val description = state.value.description

                if (title.isBlank() || description.isBlank()) {
                    return
                }

                val note = Note(
                    title = title,
                    description = description
                )

                viewModelScope.launch {
                    dao.upsertNote(note)
                }

                _state.update { it.copy(
                    isAddingNote = false,
                    title = "",
                    description = "",
                ) }

            }
            is NoteEvent.SetTitle -> {
                _state.update { it.copy(
                    title = event.title
                ) }
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
        }
    }

}