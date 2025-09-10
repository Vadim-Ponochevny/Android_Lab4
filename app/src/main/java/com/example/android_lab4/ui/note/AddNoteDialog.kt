package com.example.android_lab4.ui.note

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.android_lab4.R
import com.example.android_lab4.databinding.DialogAddNoteBinding
import kotlin.getValue

class AddNoteDialog() : DialogFragment() {

    var title: String? = null
    var description: String? = null

    private var _binding: DialogAddNoteBinding? = null
    private val binding
        get() = _binding!!

    internal lateinit var listener: NoticeDialogListener

    interface NoticeDialogListener {
        fun onDialogPositiveClick(title: String, description: String)
        fun onDialogNegativeClick()
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface.
        try {
            // Instantiate the NoticeDialogListener so you can send events to
            // the host.
            listener = context as NoticeDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface. Throw exception.
            throw ClassCastException((context.toString() +
                    " must implement NoticeDialogListener"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            title = it.getString(ARG_TITLE)
            description = it.getString(ARG_DESCRIPTION)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogAddNoteBinding.inflate(layoutInflater)

        val dialog = AlertDialog.Builder(requireContext())
            .setTitle("New note")
            .setView(binding.root)
            .setPositiveButton("Save",
                DialogInterface.OnClickListener { dialog, id ->
                    val title = binding.titleEditText.toString()
                    val description = binding.descriptionEditText.toString()
                    listener.onDialogPositiveClick(title, description)
                })
            .setNegativeButton("Cancel",
                DialogInterface.OnClickListener { _, _ ->
                    // Send the negative button event back to the
                    // host activity.
                    listener.onDialogNegativeClick()
                })
//            .setCancelable(true)
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