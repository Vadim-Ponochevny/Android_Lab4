package com.example.android_lab4

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_lab4.databinding.NoteScreenBinding
import com.example.android_lab4.ui.note.AddNoteDialog
import com.example.android_lab4.ui.note.NoteAdapter
import com.example.android_lab4.ui.note.NoteEvent
import com.example.android_lab4.ui.note.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), AddNoteDialog.NoticeDialogListener
{
    private val viewModel: NoteViewModel by viewModels()
    private var _binding: NoteScreenBinding? = null
    private val binding get() = _binding!!
    private val adapter = NoteAdapter (
        onDeleteClick = { note -> viewModel.onEvent(NoteEvent.DeleteNote(note))},
        onEditNote = {note -> viewModel.onEvent(NoteEvent.EditNote(note)); viewModel.onEvent(NoteEvent.ShowDialog)}
    )


    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)
        _binding = NoteScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        setupSystemBarsPadding(binding)
        setRView()

        pushAllNotesInAdapter()
        noteDialog()

        binding.fab.setOnClickListener {
            viewModel.onEvent(NoteEvent.ShowDialog)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupSystemBarsPadding(binding: NoteScreenBinding) {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setRView() {
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter
    }

    private fun pushAllNotesInAdapter() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.notes.collect { notes ->
                    adapter.submitList(notes)
                }
            }
        }
    }

    override fun onDialogPositiveClick(title: String, description: String) {
        viewModel.onEvent(NoteEvent.SaveNote(title, description))
        viewModel.onEvent(NoteEvent.HideDialog)
    }

    override fun onDialogNegativeClick() {
        viewModel.onEvent(NoteEvent.HideDialog)
    }

    private fun noteDialog() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    val existingDialog = supportFragmentManager
                        .findFragmentByTag("NoticeDialogFragment") as? AddNoteDialog

                    if (state.dialogIsOpen) {
                        if (existingDialog == null) {
                            val dialog = AddNoteDialog.newInstance(
                                state.title,
                                state.description
                            )
                            dialog.show(supportFragmentManager, "NoticeDialogFragment")
                        }
                    } else {
                        existingDialog?.dismiss()
                    }
                }
            }
        }
    }
}