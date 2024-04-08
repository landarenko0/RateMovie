package com.example.ratemovie.presentation.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.ratemovie.domain.entities.Movie

class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie) = oldItem == newItem
}