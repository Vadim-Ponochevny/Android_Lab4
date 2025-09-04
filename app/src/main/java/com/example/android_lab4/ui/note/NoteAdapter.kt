package com.example.android_lab4.ui.note

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android_lab4.data.model.Note
import com.example.android_lab4.databinding.OneNoteBinding
import com.example.android_lab4.ui.note.NoteAdapter.ViewHolder
import jakarta.inject.Inject

class NoteAdapter @Inject constructor(
    private val onDeleteClick: (Note) -> Unit,
    private val onEditNote: (Note) -> Unit
) : ListAdapter<Note, ViewHolder>(NoteCallBack()) {

    private lateinit var binding: OneNoteBinding

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    inner class ViewHolder(val binding: OneNoteBinding) : RecyclerView.ViewHolder (binding.root) {
        fun bind(item: Note) {
            binding.NoteTitle.text = item.title.toString()
            binding.buttonDelete.setOnClickListener {
                onDeleteClick(item)
            }
            binding.root.setOnClickListener {
                onEditNote(item)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = OneNoteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }
}

class NoteCallBack() : DiffUtil.ItemCallback<Note>() {
    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return newItem.id == oldItem.id
    }

    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return newItem == oldItem
    }
}