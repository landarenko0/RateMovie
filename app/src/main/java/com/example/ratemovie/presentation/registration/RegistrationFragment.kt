package com.example.ratemovie.presentation.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.hilt.navigation.fragment.hiltNavGraphViewModels
import androidx.navigation.fragment.findNavController
import com.example.ratemovie.R
import com.example.ratemovie.databinding.RegistrationFragmentBinding
import com.example.ratemovie.domain.remote.RegistrationResult
import com.example.ratemovie.domain.remote.RemoteResult
import com.example.ratemovie.presentation.loader.LoaderDialogFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegistrationFragment : Fragment() {

    private var _binding: RegistrationFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("RegistrationFragmentBinding was null")

    private val viewModel: RegistrationViewModel by hiltNavGraphViewModels(R.id.registrationFragment)

    private val username get() = binding.etUsername.text.toString()
    private val email get() = binding.etEmail.text.toString()
    private val password get() = binding.etPassword.text.toString()

    private val loader = LoaderDialogFragment()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = RegistrationFragmentBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setOnClickListener()
        observeViewModel()
    }

    private fun setOnClickListener() {
        binding.btnRegister.setOnClickListener {
            viewModel.signUp(username, email, password)
        }
    }

    private fun observeViewModel() {
        viewModel.registrationResult.observe(viewLifecycleOwner) { result ->
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
                        RegistrationResult.Error.EMAIL_COLLISION -> showToast(R.string.email_collision_error)
                        RegistrationResult.Error.EMPTY_FIELDS -> showToast(R.string.empty_fields_error)
                        RegistrationResult.Error.INVALID_CREDENTIALS -> showToast(R.string.invalid_email_error)
                        RegistrationResult.Error.INVALID_SYMBOLS -> showToast(R.string.invalid_symbols_error)
                        RegistrationResult.Error.USERNAME_COLLISION -> showToast(R.string.username_collision_error)
                        RegistrationResult.Error.WEAK_PASSWORD -> showToast(R.string.weak_password_error)
                        RegistrationResult.Error.DEFAULT -> showToast(R.string.default_error)
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

    private fun showToast(resId: Int) = Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()

    private fun closeFragment() {
        findNavController().popBackStack(R.id.navigation_account, inclusive = false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}