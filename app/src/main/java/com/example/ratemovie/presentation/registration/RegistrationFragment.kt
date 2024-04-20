package com.example.ratemovie.presentation.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.ratemovie.presentation.activity.MainActivityViewModel
import com.example.ratemovie.R
import com.example.ratemovie.databinding.RegistrationFragmentBinding
import com.example.ratemovie.domain.entities.RegistrationResult

class RegistrationFragment : Fragment() {

    private var _binding: RegistrationFragmentBinding? = null
    private val binding
        get() = _binding ?: throw RuntimeException("RegistrationFragmentBinding was null")

    private val viewModel: RegistrationViewModel by navGraphViewModels(R.id.registrationFragment)
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    private val username get() = binding.etUsername.text.toString()
    private val email get() = binding.etEmail.text.toString()
    private val password get() = binding.etPassword.text.toString()

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
        viewModel.loginResult.observe(viewLifecycleOwner) { result ->
            when (result) {
                is RegistrationResult.Success -> {
                    activityViewModel.user.value = result.user

                    closeFragment()
                }

                RegistrationResult.Error.EmailCollision -> showMessage(R.string.email_collision_error)
                RegistrationResult.Error.EmptyFields -> showMessage(R.string.empty_fields_error)
                RegistrationResult.Error.InvalidCredentials -> showMessage(R.string.invalid_email_error)
                RegistrationResult.Error.InvalidSymbols -> showMessage(R.string.invalid_symbols_error)
                RegistrationResult.Error.UsernameCollision -> showMessage(R.string.username_collision_error)
                RegistrationResult.Error.WeakPassword -> showMessage(R.string.weak_password_error)
                RegistrationResult.Error.Default -> showMessage(R.string.default_error)
            }
        }
    }

    private fun showMessage(resId: Int) = Toast.makeText(context, resId, Toast.LENGTH_SHORT).show()

    private fun closeFragment() {
        findNavController().popBackStack(R.id.navigation_account, inclusive = false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}