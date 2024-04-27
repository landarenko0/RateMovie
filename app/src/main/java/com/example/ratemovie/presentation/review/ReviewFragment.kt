package com.example.ratemovie.presentation.review

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ratemovie.R
import com.example.ratemovie.databinding.ReviewFragmentBinding
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.domain.utils.Globals
import com.example.ratemovie.presentation.loader.LoaderDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private var _binding: ReviewFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("ReviewFragmentBinding was null")

    private val viewModel: ReviewViewModel by hiltNavGraphViewModels(R.id.reviewFragment)

    private val reviewText get() = binding.etReviewText.text.toString()
    private val grade get() = binding.rbRating.rating.toInt()

    private val loader = LoaderDialogFragment()

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

        checkUserIsNotNull()
        setInfo(review, movie)
        setOnClickListener(review, movie)
        observeViewModel()
    }

    private fun checkUserIsNotNull() {
        if (Globals.User == null) closeFragment()
    }

    private fun setInfo(review: Review?, movie: Movie) {
        with(binding) {
            etReviewText.setText(review?.text)

            if (review != null) {
                rbRating.rating = review.grade.toFloat()
            } else {
                btnDeleteReview.visibility = View.GONE
            }

            tvMovieTitle.text = movie.title
        }
    }

    private fun setOnClickListener(review: Review?, movie: Movie) {
        binding.btnSaveReview.setOnClickListener {
            viewModel.saveReview(reviewText, grade, movie.id)
        }

        if (review != null) {
            binding.btnDeleteReview.setOnClickListener {
                viewModel.deleteReview(review, movie.id)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.shouldCloseFragment.observe(viewLifecycleOwner) { closeFragment() }

        viewModel.shouldShowLoader.observe(viewLifecycleOwner) { show ->
            if (show) showLoader() else closeLoader()
        }
    }

    private fun showLoader() {
        loader.show(childFragmentManager, null)
    }

    private fun closeLoader() {
        if (loader.isAdded) loader.dismiss()
    }

    private fun closeFragment() = findNavController().navigateUp()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}