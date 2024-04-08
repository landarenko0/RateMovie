package com.example.ratemovie.presentation.adapters

import androidx.recyclerview.widget.RecyclerView
import com.example.ratemovie.databinding.ReviewBinding
import com.example.ratemovie.domain.entities.Review

class ReviewViewHolder(private val binding: ReviewBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(review: Review) {
        binding.tvUsername.text = review.username
        binding.tvGrade.text = review.grade.toString()
        binding.tvReview.text = review.text
    }
}