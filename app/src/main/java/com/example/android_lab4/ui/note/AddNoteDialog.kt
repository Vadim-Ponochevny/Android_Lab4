package com.example.android_lab4.ui.note

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.android_lab4.R
import com.example.android_lab4.databinding.DialogAddNoteBinding
import kotlin.getValue

class AddNoteDialog : DialogFragment() {

    private var _binding: DialogAddNoteBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: NoteViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddNoteBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("Новая заметка")
            .setView(binding.root)
            .setPositiveButton("Сохранить") { _, _ ->
                val title = binding.titleEditText.text.toString()
                val description = binding.descriptionEditText.text.toString()
                Log.d("data_from_dialog", title)

                viewModel.onEvent(NoteEvent.SetTitle(title))
                viewModel.onEvent(NoteEvent.SetDescription(description))
                viewModel.onEvent(NoteEvent.SaveNote)

            }
            .setNegativeButton("Отмена") { _, _ ->
                viewModel.onEvent(NoteEvent.HideDialog)
            }
            .setCancelable(true)
            .create()

        return dialog

        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.blue)
            )
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.red)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.onEvent(NoteEvent.HideDialog)
    }
}