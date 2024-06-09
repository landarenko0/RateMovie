package com.example.ratemovie.presentation.review

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.ratemovie.R
import com.example.ratemovie.databinding.ReviewFragmentBinding
import com.example.ratemovie.domain.entities.Review
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.domain.utils.Globals.User
import com.example.ratemovie.presentation.loader.LoaderDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReviewFragment : Fragment() {

    private var _binding: ReviewFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("ReviewFragmentBinding was null")

    private val args: ReviewFragmentArgs by navArgs()

    private val viewModel: ReviewViewModel by hiltNavGraphViewModels(R.id.reviewFragment) { factory: ReviewViewModel.Factory ->
        factory.build(args.movie.id)
    }

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
        super.onViewCreated(view, savedInstanceState)

        setMovieInfo()
        setListeners()
        observeLiveData()
        viewModel.checkUserReview()
    }

    private fun setMovieInfo() {
        binding.tvMovieTitle.text = args.movie.title
    }

    private fun setListeners() {
        binding.btnSaveReview.setOnClickListener {
            viewModel.saveReview(reviewText, grade)
        }

        binding.btnDeleteReview.setOnClickListener {
            viewModel.deleteReview()
        }

        binding.etReviewText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let{ viewModel.onReviewTextChanged(s) }
            }

            override fun afterTextChanged(s: Editable?) { }
        })
    }

    private fun observeLiveData() {
        User.observe(viewLifecycleOwner) { if (it == null) closeFragment() }

        viewModel.userReview.observe(viewLifecycleOwner) { result ->
            when (result) {
                RemoteResult.Loading -> showLoader()
                is RemoteResult.Success -> {
                    closeLoader()
                    setupUserReview(result.data)
                }

                is RemoteResult.Error -> {
                    closeLoader()
                    showMessage(getString(R.string.request_failed))
                }
            }
        }

        viewModel.result.observe(viewLifecycleOwner) { result ->
            when (result) {
                RemoteResult.Loading -> showLoader()
                is RemoteResult.Success -> {
                    if (result.data != null) {
                        closeLoader()
                        closeFragment()
                    }
                }

                is RemoteResult.Error -> {
                    closeLoader()
                    showMessage(result.message)
                }
            }
        }

        viewModel.symbolsLeft.observe(viewLifecycleOwner) { symbolsLeft ->
            binding.tvSymbolsLeft.text = getString(R.string.symbols_left, symbolsLeft)
        }
    }

    private fun setupUserReview(review: Review?) {
        with(binding) {
            if (review == null) {
                rbRating.rating = 0f
                etReviewText.setText("")
            } else {
                rbRating.rating = review.grade.toFloat()
                etReviewText.setText(review.text)
            }
        }

        binding.btnDeleteReview.visibility = View.VISIBLE
    }

    private fun showMessage(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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