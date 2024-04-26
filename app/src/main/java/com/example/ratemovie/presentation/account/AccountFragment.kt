package com.example.ratemovie.presentation.account

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.ratemovie.R
import com.example.ratemovie.domain.entities.Movie
import com.example.ratemovie.databinding.AccountFragmentBinding
import com.example.ratemovie.domain.utils.Globals
import com.example.ratemovie.presentation.adapters.MoviesAdapter

class AccountFragment : Fragment() {

    private var _binding: AccountFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("AccountFragmentBinding was null")

    private val viewModel: AccountViewModel by navGraphViewModels(R.id.navigation_account)

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
        checkUserIsNotNull()
        setupRecyclerViews()
        setupButtons()
        setupRadioButtons()
        observeViewModel()
    }

    private fun checkUserIsNotNull() {
        val user = Globals.User

        with(binding) {
            if (user != null) {
                tvUsername.text = user.username
                tvEmail.text = user.email

                viewModel.getUserLikedMovies(user.liked)
                viewModel.getUserReviewedMovies(user.reviewed)

                btnSignOut.visibility = View.VISIBLE
                btnSignIn.visibility = View.GONE
            }
            else {
                tvUsername.text = getString(R.string.unauthorized)

                tvEmail.visibility = View.GONE
                btnSignOut.visibility = View.GONE
                btnSignIn.visibility = View.VISIBLE
            }
        }
    }

    private fun setupRadioButtons() {
        binding.rbLightTheme.isChecked = true
        binding.rgRadioButtons.setOnCheckedChangeListener { _, id ->
            // TODO: Смена темы
            when (id) {
                binding.rbLightTheme.id -> {
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }

                binding.rbDarkTheme.id -> {
                    //AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }

                else -> throw IllegalStateException("Unknown radio button id $id")
            }
        }
    }

    private fun setupButtons() {
        with(binding) {
            btnSignOut.setOnClickListener {
                viewModel.signOut()
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

    private fun observeViewModel() {
        viewModel.likedMovies.observe(viewLifecycleOwner) { movies ->
            with(binding) {
                if (!movies.isNullOrEmpty()) {
                    tvYouLike.visibility = View.VISIBLE
                    rvLikedMovies.visibility = View.VISIBLE

                    likedMoviesAdapter.submitList(movies)
                } else {
                    tvYouLike.visibility = View.GONE
                    rvLikedMovies.visibility = View.GONE
                }
            }
        }

        viewModel.reviewedMovies.observe(viewLifecycleOwner) { movies ->
            with(binding) {
                if (!movies.isNullOrEmpty()) {
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

    private fun showMovieDetailsFragment(movie: Movie) {
        val action = AccountFragmentDirections.actionNavigationAccountToMovieDetailsFragment(movie)
        //val action = AccountFragmentDirections.actionNavigationAccountToNavMovieDetails()

        findNavController().navigate(action)
    }

    private fun showLoginFragment() {
        val action = AccountFragmentDirections.actionNavigationAccountToLoginFragment()
        //val action = AccountFragmentDirections.actionAccountToLoginFragment()

        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}