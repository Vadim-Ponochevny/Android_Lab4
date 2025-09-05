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

class AddNoteDialog(val title: String? = null, val description: String? = null) : DialogFragment() {

    private var _binding: DialogAddNoteBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: NoteViewModel by activityViewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddNoteBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("New note")
            .setView(binding.root)
            .setPositiveButton("Save") { _, _ ->
                val title = binding.titleEditText.text.toString()
                val description = binding.descriptionEditText.text.toString()
                Log.d("data_from_dialog", title)

                viewModel.onEvent(NoteEvent.SetTitle(title))
                viewModel.onEvent(NoteEvent.SetDescription(description))
                viewModel.onEvent(NoteEvent.SaveNote)

            }
            .setNegativeButton("Cancel") { _, _ ->
                viewModel.onEvent(NoteEvent.HideDialog)
            }
            .setCancelable(true)
            .create()


        dialog.setOnShowListener {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.blue)
            )
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE)?.setTextColor(
                ContextCompat.getColor(requireContext(), R.color.gray_gray)
            )
        }

        dialog.window?.setBackgroundDrawableResource(R.drawable.shape)

        val title = arguments?.getString(ARG_TITLE)
        val description = arguments?.getString(ARG_DESCRIPTION)

        if (!title.isNullOrEmpty()) {
            binding.titleEditText.setText(title)
            binding.titleEditText.setSelection(binding.titleEditText.text.length)
            dialog.setTitle("Edit")
        }
        if (!description.isNullOrEmpty()) {
            binding.descriptionEditText.setText(description)
        }
        return dialog
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        viewModel.onEvent(NoteEvent.HideDialog)
    }

    companion object {
        private const val ARG_TITLE = "arg_title"
        private const val ARG_DESCRIPTION = "arg_description"

        fun newInstance(title: String? = null, description: String? = null): AddNoteDialog {
            val fragment = AddNoteDialog()
            val args = Bundle().apply {
                putString(ARG_TITLE, title)
                putString(ARG_DESCRIPTION, description)
            }
            fragment.arguments = args
            return fragment
        }
    }
}