package com.example.omdb.ui.fragment.home.adapter.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.omdb.data.model.entity.Movie

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        // data class make this easy
        return oldItem == newItem
    }
}