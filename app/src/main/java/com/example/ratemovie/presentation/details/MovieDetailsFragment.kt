package com.example.ratemovie.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import coil.load
import com.example.ratemovie.presentation.MainActivityViewModel
import com.example.ratemovie.R
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.databinding.MovieDetailsFragmentBinding
import com.example.ratemovie.presentation.adapters.ReviewsAdapter

class MovieDetailsFragment : Fragment() {

    // TODO: Добавление фильма в избранное

    private var _binding: MovieDetailsFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("MovieDetailsFragmentBinding was null")

    private val args: MovieDetailsFragmentArgs by navArgs()

    private val viewModel: MovieDetailsViewModel by navGraphViewModels(R.id.movieDetailsFragment) {
        MovieDetailsViewModelFactory(args.movie.id)
    }
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private val reviewsAdapter = ReviewsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = MovieDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.updateData()

        setupMovieInfo(args.movie)
        setupRecyclerView()
        setOnEditButtonClickListener(args.movie)
        observeViewModel()
    }

    private fun setupMovieInfo(movie: Movie) {
        with(binding) {
            ivMoviePoster.load(movie.posterUrl?.url)
            tvMovieTitle.text = movie.title
            tvGenres.text =
                movie.genres.joinToString(", ") { it.name }.replaceFirstChar { it.uppercase() }
            tvDescription.text = movie.description
        }
    }

    private fun setupRecyclerView() {
        binding.rvReviews.adapter = reviewsAdapter
    }

    private fun setOnEditButtonClickListener(movie: Movie) {
        binding.ibEditReview.setOnClickListener {
            showReviewFragment(
                review = viewModel.userReview.value,
                movie = movie
            )
        }
    }

    private fun observeViewModel() {
        activityViewModel.user.observe(viewLifecycleOwner) { user ->
            if (user == null) {
                binding.ibFavorite.visibility = View.GONE
                binding.ibEditReview.visibility = View.GONE
            } else {
                binding.ibFavorite.visibility = View.VISIBLE
                binding.ibEditReview.visibility = View.VISIBLE
            }
        }

        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            with(binding) {
                if (reviews.isEmpty()) {
                    tvReviewsCount.text = getString(R.string.no_reviews)
                    tvReviewsCount.visibility = View.VISIBLE
                    tvReviews.visibility = View.GONE
                    rvReviews.visibility = View.GONE
                } else {
                    tvReviews.text = getString(R.string.reviews)
                    reviewsAdapter.submitList(reviews)
                    tvReviewsCount.text = getString(R.string.reviewsCount, reviews.size)
                    tvReviewsCount.visibility = View.VISIBLE
                }
            }
        }

        viewModel.movieRating.observe(viewLifecycleOwner) { rating ->
            if (rating != null) {
                binding.rbRating.rating = rating
                binding.rbRating.visibility = View.VISIBLE
            }
        }

        viewModel.userReview.observe(viewLifecycleOwner) { review ->
            if (activityViewModel.user.value != null) {
                if (review != null) {
                    binding.ibEditReview.setImageResource(R.drawable.ic_edit_24)
                } else {
                    binding.ibEditReview.setImageResource(R.drawable.ic_add_24)
                }
            }
        }
    }

    private fun showReviewFragment(review: Review?, movie: Movie) {
        val action =
            MovieDetailsFragmentDirections.actionMovieDetailsFragmentToReviewFragment(movie, review)

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}