package com.example.ratemovie.presentation.adapters

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ratemovie.databinding.MovieBinding
import com.example.ratemovie.domain.entities.Movie

class MovieViewHolder(
    private val binding: MovieBinding,
    private val onMovieClickListener: ((Movie) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        with(binding) {
            ivMoviePoster.load(movie.posterUrl?.url)
            tvMovieTitle.text = movie.title
            root.setOnClickListener { onMovieClickListener?.invoke(movie) }
        }
    }
}