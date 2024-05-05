package com.example.ratemovie.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.ratemovie.R
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.databinding.MovieDetailsFragmentBinding
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.utils.Globals
import com.example.ratemovie.presentation.loader.LoaderDialogFragment
import com.example.ratemovie.presentation.adapters.ReviewsAdapter

class MovieDetailsFragment : Fragment() {

    private var _binding: MovieDetailsFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("MovieDetailsFragmentBinding was null")

    private val args: MovieDetailsFragmentArgs by navArgs()

    private val viewModel: MovieDetailsViewModel by hiltNavGraphViewModels(R.id.movieDetailsFragment) { factory: MovieDetailsViewModel.Factory ->
        factory.build(args.movie.id)
    }

    private val reviewsAdapter = ReviewsAdapter()

    private val loader = LoaderDialogFragment()

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

        checkUserIsNotNull()
        setupMovieInfo()
        setupRecyclerView()
        setupOnClickListeners()
        observeViewModel()
    }

    private fun checkUserIsNotNull() {
        val user = Globals.User

        with(binding) {
            if (user != null) {
                ibFavorite.visibility = View.VISIBLE
                ibEditReview.visibility = View.VISIBLE
            } else {
                ibFavorite.visibility = View.GONE
                ibEditReview.visibility = View.GONE
            }
        }
    }

    private fun setupMovieInfo() {
        val movie = args.movie

        with(binding) {
            ivMoviePoster.load(movie.posterUrl?.url)
            tvMovieTitle.text = movie.title
            tvGenres.text =
                movie.genres.joinToString(", ") { it.name }.replaceFirstChar { it.uppercase() }
            tvDescription.text = movie.description
        }
    }

    private fun setupOnClickListeners() {
        val movie = args.movie

        binding.ibFavorite.setOnClickListener { viewModel.onFavoriteButtonClicked(movie.id) }
    }

    private fun setupRecyclerView() {
        binding.rvReviews.adapter = reviewsAdapter
    }

    private fun observeViewModel() {
        viewModel.reviews.observe(viewLifecycleOwner) { result ->
            when(result) {
                RemoteResult.Loading -> { showLoader() }

                is RemoteResult.Success -> {
                    val reviews = result.data

                    with(binding) {
                        if (reviews.isEmpty()) {
                            tvReviewsCount.text = getString(R.string.no_reviews)
                            tvReviews.visibility = View.GONE
                        } else {
                            reviewsAdapter.submitList(reviews)
                            tvReviewsCount.text = getString(R.string.reviewsCount, reviews.size)
                            tvReviews.visibility = View.VISIBLE
                        }
                    }

                    closeLoader()
                }

                is RemoteResult.Error -> { closeLoader() }
            }
        }

        viewModel.userReview.observe(viewLifecycleOwner) { result ->
            when (result) {
                RemoteResult.Loading -> { showLoader() }

                is RemoteResult.Success -> {
                    val review = result.data
                    val movie = args.movie

                    binding.ibEditReview.setOnClickListener {
                        showReviewFragment(
                            review = review,
                            movie = movie
                        )
                    }

                    if (Globals.User != null) {
                        if (review != null) {
                            binding.ibEditReview.setImageResource(R.drawable.ic_edit_24)
                        } else {
                            binding.ibEditReview.setImageResource(R.drawable.ic_add_24)
                        }
                    }

                    closeLoader()
                }

                is RemoteResult.Error -> { closeLoader() }
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { movieIsFavorite ->
            if (movieIsFavorite) {
                binding.ibFavorite.setImageResource(R.drawable.ic_favorite_filled_24)
            } else {
                binding.ibFavorite.setImageResource(R.drawable.ic_favorite_24)
            }
        }
    }

    private fun showLoader() {
        loader.show(childFragmentManager, null)
    }

    private fun closeLoader() {
        if (loader.isAdded) loader.dismiss()
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