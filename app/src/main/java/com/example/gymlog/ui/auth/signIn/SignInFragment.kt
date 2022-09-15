package com.example.gymlog.ui.auth.signIn

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.gymlog.R
import com.example.gymlog.base.BaseFragment
import com.example.gymlog.databinding.FragmentSignInBinding
import com.example.gymlog.ui.MainActivity


class SignInFragment : BaseFragment() {

    override val _viewModel: SignInViewModel by viewModels {
        SignInViewModel.Factory(
            requireActivity().application
        )
    }
    private lateinit var binding: FragmentSignInBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val intent = Intent(requireContext(), MainActivity::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        binding.viewModel = _viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.tvSubFooter.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        binding.btnSignIn.setOnClickListener {
            _viewModel.handleSignIn(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString()
            ) {
                startActivity(intent)
            }
        }

        binding.etEmail.addTextChangedListener { _viewModel.resetErrors(binding.etEmail) }

        binding.etPassword.addTextChangedListener { _viewModel.resetErrors(binding.etPassword) }

        return binding.root
    }



}