package com.example.ratemovie.presentation.recyclerview.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.databinding.SearchMovieBinding
import com.example.ratemovie.presentation.recyclerview.viewholders.SearchMovieViewHolder
import com.example.ratemovie.presentation.recyclerview.diffcallback.MovieDiffCallback

class SearchMoviesAdapter : ListAdapter<Movie, SearchMovieViewHolder>(MovieDiffCallback()) {

    var onMovieClickListener: ((Movie) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchMovieViewHolder {
        val viewHolderBinding =
            SearchMovieBinding.inflate(LayoutInflater.from(parent.context))

        return SearchMovieViewHolder(viewHolderBinding, onMovieClickListener)
    }

    override fun onBindViewHolder(holder: SearchMovieViewHolder, position: Int) {
        val movie = getItem(position)

        holder.bind(movie)
    }
}