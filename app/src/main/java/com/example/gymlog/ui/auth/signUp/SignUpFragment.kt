package com.example.gymlog.ui.auth.signUp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.gymlog.R
import com.example.gymlog.base.BaseFragment
import com.example.gymlog.databinding.FragmentSignUpBinding
import com.example.gymlog.ui.MainActivity

class SignUpFragment : BaseFragment() {

    override val _viewModel: SignUpViewModel by viewModels{
        SignUpViewModel.Factory(
            requireActivity().application
        )
    }

    private lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = _viewModel
        binding.tvSubFooter.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }


        binding.btnSignUp.setOnClickListener {
            _viewModel.handleSignUp(
                binding.etEmail.text.toString(),
                binding.etPassword.text.toString(),
                binding.etName.text.toString()
            ) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }
        }


        binding.etEmail.addTextChangedListener { _viewModel.resetErrors(binding.etEmail) }
        binding.etPassword.addTextChangedListener { _viewModel.resetErrors(binding.etPassword) }
        binding.etName.addTextChangedListener { _viewModel.resetErrors(binding.etName) }
        return binding.root
    }

}