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
import com.example.ratemovie.presentation.activity.MainActivityViewModel
import com.example.ratemovie.R
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.databinding.MovieDetailsFragmentBinding
import com.example.ratemovie.presentation.loader.LoaderDialogFragment
import com.example.ratemovie.presentation.adapters.ReviewsAdapter

class MovieDetailsFragment : Fragment() {

    // TODO: Добавить лоадер при запуске фрагмента

    private var _binding: MovieDetailsFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("MovieDetailsFragmentBinding was null")

    private val args: MovieDetailsFragmentArgs by navArgs()

    private val viewModel: MovieDetailsViewModel by navGraphViewModels(R.id.movieDetailsFragment) {
        MovieDetailsViewModelFactory(args.movie.id)
    }
    private val activityViewModel: MainActivityViewModel by activityViewModels()

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

        setupMovieInfo(args.movie)
        setupRecyclerView()
        setupOnClickListeners(args.movie)
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

    private fun setupOnClickListeners(movie: Movie) {
        binding.ibEditReview.setOnClickListener {
            showReviewFragment(
                review = viewModel.userReview.value,
                movie = movie
            )
        }

        binding.ibFavorite.setOnClickListener {
            val userLikesMovie = viewModel.isFavorite.value ?: return@setOnClickListener

            if (userLikesMovie) {
                viewModel.deleteFromFavorites(movie.id)
                activityViewModel.deleteMovieFromFavorites(movie)
            }
            else {
                viewModel.addToFavorites(movie.id)
                activityViewModel.addMovieToFavorites(movie)
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvReviews.adapter = reviewsAdapter
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
                    tvReviews.visibility = View.GONE
                } else {
                    reviewsAdapter.submitList(reviews)
                    tvReviewsCount.text = getString(R.string.reviewsCount, reviews.size)
                    tvReviews.visibility = View.VISIBLE
                }
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

        viewModel.isFavorite.observe(viewLifecycleOwner) { movieIsFavorite ->
            if (movieIsFavorite) {
                binding.ibFavorite.setImageResource(R.drawable.ic_favorite_filled_24)
            }
            else {
                binding.ibFavorite.setImageResource(R.drawable.ic_favorite_24)
            }
        }

        viewModel.shouldShowLoader.observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) {
                showLoader()
            }
            else {
                closeLoader()
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