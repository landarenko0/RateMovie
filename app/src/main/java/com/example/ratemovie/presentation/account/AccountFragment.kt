package com.example.ratemovie.presentation.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.example.ratemovie.R
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.databinding.AccountFragmentBinding
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.utils.Globals.User
import com.example.ratemovie.presentation.recyclerview.adapters.MoviesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccountFragment : Fragment() {

    private var _binding: AccountFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("AccountFragmentBinding was null")

    private val viewModel: AccountViewModel by hiltNavGraphViewModels(R.id.accountFragment)

    private val likedMoviesAdapter = MoviesAdapter()
    private val reviewedMoviesAdapter = MoviesAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = AccountFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerViews()
        setupButtons()
        observeLiveData()
    }

    private fun setupButtons() {
        with(binding) {
            btnSignOut.setOnClickListener {
                viewModel.signOut()

                tvUsername.text = getString(R.string.unauthorized)
                tvEmail.visibility = View.GONE
                btnSignIn.visibility = View.VISIBLE
                btnSignOut.visibility = View.GONE
            }

            btnSignIn.setOnClickListener {
                showLoginFragment()
            }
        }
    }

    private fun setupRecyclerViews() {
        likedMoviesAdapter.onMovieClickListener = { showMovieDetailsFragment(it) }
        reviewedMoviesAdapter.onMovieClickListener = { showMovieDetailsFragment(it) }

        with(binding) {
            rvLikedMovies.adapter = likedMoviesAdapter
            rvReviewedMovies.adapter = reviewedMoviesAdapter
        }
    }

    private fun observeLiveData() {
        User.observe(viewLifecycleOwner) { user ->
            with(binding) {
                if (user == null) {
                    tvUsername.text = getString(R.string.unauthorized)

                    tvEmail.visibility = View.GONE
                    btnSignOut.visibility = View.GONE
                    btnSignIn.visibility = View.VISIBLE

                    rvLikedMovies.visibility = View.GONE
                    rvReviewedMovies.visibility = View.GONE

                    tvYouLike.visibility = View.GONE
                    tvYouLeftReview.visibility = View.GONE
                } else {
                    tvUsername.text = user.username
                    tvEmail.text = user.email
                    tvEmail.visibility = View.VISIBLE

                    btnSignOut.visibility = View.VISIBLE
                    btnSignIn.visibility = View.GONE

                    viewModel.getUserLikedMovies(user.liked)
                    viewModel.getUserReviewedMovies(user.reviewed)
                }
            }
        }

        viewModel.likedMovies.observe(viewLifecycleOwner) { result ->
            when (result) {
                RemoteResult.Loading -> { }

                is RemoteResult.Success -> {
                    if (result.data != null) {
                        val movies = result.data

                        with(binding) {
                            if (movies.isNotEmpty()) {
                                tvYouLike.visibility = View.VISIBLE
                                rvLikedMovies.visibility = View.VISIBLE

                                likedMoviesAdapter.submitList(movies)
                            } else {
                                tvYouLike.visibility = View.GONE
                                rvLikedMovies.visibility = View.GONE
                            }
                        }
                    }
                }

                is RemoteResult.Error -> showToast(getString(R.string.unable_download_liked_movies))
            }
        }

        viewModel.reviewedMovies.observe(viewLifecycleOwner) { result ->
            when (result) {
                RemoteResult.Loading -> { }

                is RemoteResult.Success -> {
                    if (result.data != null) {
                        val movies = result.data

                        with(binding) {
                            if (movies.isNotEmpty()) {
                                tvYouLeftReview.visibility = View.VISIBLE
                                rvReviewedMovies.visibility = View.VISIBLE

                                reviewedMoviesAdapter.submitList(movies)
                            } else {
                                tvYouLeftReview.visibility = View.GONE
                                rvReviewedMovies.visibility = View.GONE
                            }
                        }
                    }
                }

                is RemoteResult.Error -> showToast(getString(R.string.unable_download_reviewed_movies))
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun showMovieDetailsFragment(movie: Movie) {
        val action = AccountFragmentDirections.actionAccountFragmentToMovieDetailsNav(movie)

        findNavController().navigate(action)
    }

    private fun showLoginFragment() {
        val action = AccountFragmentDirections.actionAccountFragmentToLoginFragment()

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}