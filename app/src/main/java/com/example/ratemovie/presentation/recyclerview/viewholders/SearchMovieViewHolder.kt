package com.example.ratemovie.presentation.recyclerview.viewholders

import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.ratemovie.databinding.SearchMovieBinding
import com.example.ratemovie.domain.entities.Movie

class SearchMovieViewHolder(
    private val binding: SearchMovieBinding,
    private val onMovieClickListener: ((Movie) -> Unit)?
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(movie: Movie) {
        binding.apply {
            ivMoviePoster.load(movie.posterUrl?.url)

            tvMovieTitle.text = movie.title

            tvGenres.text = movie.genres.joinToString(", ") { it.name }.replaceFirstChar { it.uppercase() }

            binding.root.setOnClickListener { onMovieClickListener?.invoke(movie) }
        }
    }
}