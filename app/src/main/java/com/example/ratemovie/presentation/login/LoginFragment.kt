package com.example.ratemovie.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.example.ratemovie.R
import com.example.ratemovie.databinding.LoginFragmentBinding
import com.example.ratemovie.domain.remote.LoginResult
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.presentation.loader.LoaderDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("LoginFragmentBinding was null")

    private val viewModel: LoginViewModel by hiltNavGraphViewModels(R.id.loginFragment)

    private val email get() = binding.etEmail.text.toString()
    private val password get() = binding.etPassword.text.toString()

    private val loader = LoaderDialogFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setOnClickListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is RemoteResult.Loading -> showLoader()
                is RemoteResult.Success -> {
                    if (result.data != null) {
                        closeLoader()
                        closeFragment()
                    }
                }

                is RemoteResult.Error -> {
                    viewModel.resetState()
                    closeLoader()

                    when (result.message) {
                        LoginResult.Error.EMPTY_FIELDS -> showToast(R.string.empty_fields_error)
                        LoginResult.Error.INVALID_CREDENTIALS -> showToast(R.string.invalid_data_error)
                        LoginResult.Error.DEFAULT -> showToast(R.string.default_error)
                    }
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

    private fun setOnClickListeners() {
        binding.btnSignIn.setOnClickListener { viewModel.signIn(email, password) }

        binding.btnRegistration.setOnClickListener { openRegisterFragment() }
    }

    private fun showToast(resId: Int) = Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()

    private fun openRegisterFragment() {
        val action = LoginFragmentDirections.actionLoginFragmentToRegistrationFragment()

        findNavController().navigate(action)
    }

    private fun closeFragment() = findNavController().navigateUp()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}