package com.example.gymlog.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.gymlog.R
import com.example.gymlog.databinding.FragmentSignInBinding
import com.example.gymlog.ui.MainActivity


class SignInFragment : Fragment() {

    private val authViewModel: AuthenticationViewModel by viewModels()

    private lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        binding.tvSubFooter.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }

        val email = binding.etEmail.text.toString()
        val password = binding.etPassword.text.toString()

        binding.btnSignIn.setOnClickListener {
            authViewModel.handleSignIn(email, password) {
                val intent = Intent(requireContext(), MainActivity::class.java)
                startActivity(intent)
            }

        }
        return binding.root
    }

}