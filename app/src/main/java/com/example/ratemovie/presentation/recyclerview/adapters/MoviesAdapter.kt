package com.example.ratemovie.presentation.recyclerview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.databinding.MovieBinding
import com.example.ratemovie.presentation.recyclerview.viewholders.MovieViewHolder
import com.example.ratemovie.presentation.recyclerview.diffcallback.MovieDiffCallback

class MoviesAdapter : ListAdapter<Movie, MovieViewHolder>(MovieDiffCallback()) {

    var onMovieClickListener: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val viewHolderBinding = MovieBinding.inflate(LayoutInflater.from(parent.context))

        return MovieViewHolder(viewHolderBinding, onMovieClickListener)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)

        holder.bind(movie)
    }
}