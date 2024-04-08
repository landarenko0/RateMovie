package com.example.ratemovie.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.databinding.ReviewBinding

class ReviewsAdapter : ListAdapter<Review, ReviewViewHolder>(ReviewDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val viewHolderBinding = ReviewBinding.inflate(LayoutInflater.from(parent.context))

        return ReviewViewHolder(viewHolderBinding)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = getItem(position)

        holder.bind(review)
    }
}