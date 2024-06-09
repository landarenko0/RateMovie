package com.example.ratemovie.presentation.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.ratemovie.R
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.databinding.MovieDetailsFragmentBinding
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.utils.Globals.User
import com.example.ratemovie.presentation.loader.LoaderDialogFragment
import com.example.ratemovie.presentation.recyclerview.adapters.ReviewsAdapter

class MovieDetailsFragment : Fragment() {

    private var _binding: MovieDetailsFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("MovieDetailsFragmentBinding was null")

    private val args: MovieDetailsFragmentArgs by navArgs()

    private val viewModel: MovieDetailsViewModel by hiltNavGraphViewModels(R.id.movie_details_nav) { factory: MovieDetailsViewModel.Factory ->
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
        super.onViewCreated(view, savedInstanceState)

        viewModel.updateData()

        setupMovieInfo()
        setupRecyclerView()
        setupOnClickListeners()
        observeLiveData()
        checkUserLeftReview()
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

    private fun checkUserLeftReview() {
        val userLeftReview = User.value?.reviewed?.contains(args.movie.id.toString())
        val image = if (userLeftReview == true) R.drawable.ic_edit_24 else R.drawable.ic_add_24
        binding.ibEditReview.setImageResource(image)
    }

    private fun setupOnClickListeners() {
        binding.ibFavorite.setOnClickListener { viewModel.onFavoriteButtonClicked() }
        binding.ibEditReview.setOnClickListener { showReviewFragment(args.movie) }
    }

    private fun setupRecyclerView() {
        binding.rvReviews.adapter = reviewsAdapter
    }

    private fun observeLiveData() {
        User.observe(viewLifecycleOwner) { user ->
            val visibility = if (user == null) View.GONE else View.VISIBLE

            with(binding) {
                ibFavorite.visibility = visibility
                ibEditReview.visibility = visibility
            }
        }

        viewModel.reviews.observe(viewLifecycleOwner) { result ->
            when(result) {
                RemoteResult.Loading -> showLoader()

                is RemoteResult.Success -> {
                    closeLoader()

                    if (result.data != null) {
                        val reviews = result.data

                        with(binding) {
                            if (reviews.isEmpty()) {
                                tvReviewsCount.text = getString(R.string.no_reviews)
                                tvReviews.visibility = View.GONE
                                rvReviews.visibility = View.GONE
                            } else {
                                reviewsAdapter.submitList(reviews)
                                tvReviewsCount.text = getString(R.string.reviewsCount, reviews.size)
                                tvReviews.visibility = View.VISIBLE
                                rvReviews.visibility = View.VISIBLE
                            }
                        }
                    }
                }

                is RemoteResult.Error -> {
                    closeLoader()
                    showToast(getString(R.string.unable_download_reviews))
                }
            }
        }

        viewModel.isFavorite.observe(viewLifecycleOwner) { result ->
            when (result) {
                RemoteResult.Loading -> showLoader()

                is RemoteResult.Success -> {
                    closeLoader()

                    if (result.data != null) {
                        val image = if (result.data) R.drawable.ic_favorite_filled_24 else R.drawable.ic_favorite_24
                        binding.ibFavorite.setImageResource(image)
                    }
                }

                is RemoteResult.Error -> {
                    closeLoader()
                    showToast(getString(R.string.request_failed))
                }
            }
        }
    }

    private fun showLoader() {
        loader.show(childFragmentManager, null)
    }

    private fun closeLoader() {
        if (loader.isAdded) loader.dismiss()
    }

    private fun showReviewFragment(movie: Movie) {
        val action = MovieDetailsFragmentDirections.actionMovieDetailsFragmentToReviewFragment(movie)

        findNavController().navigate(action)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}