package com.example.ratemovie.presentation.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.example.ratemovie.presentation.MainActivityViewModel
import com.example.ratemovie.R
import com.example.ratemovie.databinding.ReviewFragmentBinding
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.presentation.details.MovieDetailsViewModel

class ReviewFragment : Fragment() {

    private var _binding: ReviewFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("ReviewFragmentBinding was null")

    private val viewModel: ReviewViewModel by navGraphViewModels(R.id.reviewFragment)
    private val movieDetailsViewModel: MovieDetailsViewModel by navGraphViewModels(R.id.movieDetailsFragment)
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private val reviewText get() = binding.etReviewText.text.toString()
    private val grade get() = binding.rbRating.rating

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = ReviewFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args: ReviewFragmentArgs by navArgs()
        val review = args.review
        val movie = args.movie


        setInfo(review, movie)
        setOnClickListener(review, movie)
        observeViewModel()
    }

    private fun setInfo(review: Review?, movie: Movie) {
        with(binding) {
            etReviewText.setText(review?.text)

            if (review != null) {
                rbRating.rating = review.grade
            } else {
                btnDeleteReview.visibility = View.GONE
            }

            tvMovieTitle.text = movie.title
        }
    }

    private fun setOnClickListener(review: Review?, movie: Movie) {
        binding.btnSaveReview.setOnClickListener {
            // TODO: Показать лоадер
            viewModel.saveReview(reviewText, grade, movie.id)
            activityViewModel.addReviewedMovie(movie)
        }

        if (review != null) {
            binding.btnDeleteReview.setOnClickListener {
                // TODO: Показать лоадер
                viewModel.deleteReview(review, movie)
                activityViewModel.deleteReviewedMovie(movie)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.shouldCloseFragment.observe(viewLifecycleOwner) { closeFragment() }

        activityViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user == null) closeFragment()
        }
    }

    private fun closeFragment() = findNavController().navigateUp()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}