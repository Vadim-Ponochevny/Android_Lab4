package com.example.android_lab4

import android.annotation.SuppressLint
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
import com.example.android_lab4.ui.note.NoteState
import com.example.android_lab4.ui.note.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity(
) {
    private val viewModel: NoteViewModel by viewModels()
    private lateinit var binding: NoteScreenBinding
    private val adapter = NoteAdapter { note ->
        viewModel.onEvent(NoteEvent.DeleteNote(note))
    }
    private var addNoteDialog: AddNoteDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NoteScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        setupSystemBarsPadding(binding)
        setRView()


        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.state.collect { state ->
                    adapter.submitList(state.notes) {
                        binding.recyclerView.post { adapter.notifyDataSetChanged() }
                    }
                    Log.d("Activity_state", "Notes: ${state.notes}")

                    if (state.isAddingNote) {
                        if (addNoteDialog == null) {
                            addNoteDialog = AddNoteDialog()
                            addNoteDialog?.show(supportFragmentManager, "AddNoteDialog")
                        }
                    } else {
                        addNoteDialog?.dismiss()
                        addNoteDialog = null
                    }
                }
            }
        }

        binding.fab.setOnClickListener {
            viewModel.onEvent(NoteEvent.ShowDialog)
        }
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
}