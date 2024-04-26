package com.example.ratemovie.presentation.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.ratemovie.R
import com.example.ratemovie.databinding.LoginFragmentBinding
import com.example.ratemovie.domain.entities.LoginResult
import com.example.ratemovie.presentation.loader.LoaderDialogFragment

class LoginFragment : Fragment() {

    private var _binding: LoginFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("LoginFragmentBinding was null")

    private val viewModel: LoginViewModel by navGraphViewModels(R.id.loginFragment)

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
        setOnClickListeners()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is LoginResult.Success -> closeFragment()

                LoginResult.Error.EmptyFields -> showMessage(R.string.empty_fields_error)
                LoginResult.Error.InvalidCredentials -> showMessage(R.string.invalid_data_error)
                LoginResult.Error.Default -> showMessage(R.string.default_error)
            }
        }

        viewModel.shouldShowLoader.observe(viewLifecycleOwner) { showLoader ->
            if (showLoader) showLoader() else closeLoader()
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

    private fun showMessage(resId: Int) = Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()

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