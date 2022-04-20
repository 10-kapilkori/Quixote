package com.task.quixotetask

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.task.quixotetask.databinding.NotesAdapterBinding

private const val TAG = "NotesAdapter"

class NotesAdapter(private var list: List<Notes>, val listener: OnClickEvents) :
    RecyclerView.Adapter<NotesViewHolder>() {

    fun updatedList(list: List<Notes>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        val binding = NotesAdapterBinding.bind(
            LayoutInflater.from(parent.context).inflate(R.layout.notes_adapter, parent, false)
        )
        return NotesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder: $list")

        if (list.isNotEmpty()) {
            holder.bindView(list, position)
        }

        holder.binding.root.setOnClickListener {
            listener.onCardClick(list, position)
        }
    }

    override fun getItemCount(): Int = list.size

    interface OnClickEvents {
        fun onCardClick(list: List<Notes>, position: Int)
    }
}

class NotesViewHolder(itemView: NotesAdapterBinding) : RecyclerView.ViewHolder(itemView.root) {
    val binding = itemView

    fun bindView(list: List<Notes>, position: Int) {
        binding.titleTextTv.text = list[position].title
        binding.descriptionTextTv.text = list[position].description
        binding.noteImageIv.setImageURI(Uri.parse(list[position].imagePath))
    }
}