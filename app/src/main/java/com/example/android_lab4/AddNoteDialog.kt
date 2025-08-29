package com.example.android_lab4

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.android_lab4.databinding.DialogAddNoteBinding
import kotlin.getValue

class AddNoteDialog : DialogFragment() {

    private var _binding: DialogAddNoteBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: NoteViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddNoteBinding.inflate(layoutInflater)

        return AlertDialog.Builder(requireContext())
            .setTitle("Новая заметка")
            .setView(binding.root)
            .setPositiveButton("Сохранить") { _, _ ->
                val title = binding.titleEditText.text.toString()
                val description = binding.descriptionEditText.text.toString()

                viewModel.onEvent(NoteEvent.SetTitle(title))
                viewModel.onEvent(NoteEvent.SetDescription(description))
                viewModel.onEvent(NoteEvent.SaveNote)
            }
            .setNegativeButton("Отмена", null)
            .create()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}