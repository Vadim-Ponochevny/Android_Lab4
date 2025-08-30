package com.example.android_lab4

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.android_lab4.databinding.NoteScreenBinding
import com.example.android_lab4.ui.note.AddNoteDialog
import com.example.android_lab4.ui.note.NoteAdapter
import com.example.android_lab4.ui.note.NoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: NoteViewModel by viewModels()
    private lateinit var binding: NoteScreenBinding
    private val adapter = NoteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = NoteScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        enableEdgeToEdge()
        setupSystemBarsPadding(binding)
        setRView()

        binding.fab.setOnClickListener {
            AddNoteDialog().show(supportFragmentManager, "AddNoteDialog")
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