package com.example.ratemovie.presentation.recyclerview.diffcallback

import androidx.recyclerview.widget.DiffUtil
import com.example.ratemovie.domain.entities.Review

class ReviewDiffCallback : DiffUtil.ItemCallback<Review>() {

    override fun areItemsTheSame(oldItem: Review, newItem: Review) =
        oldItem.username == newItem.username

    override fun areContentsTheSame(oldItem: Review, newItem: Review) = oldItem == newItem
}