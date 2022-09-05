package com.example.gymlog.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
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

        val intent = Intent(requireContext(), MainActivity::class.java)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        binding.tvSubFooter.setOnClickListener {
            Navigation.findNavController(binding.root)
                .navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }


        binding.btnSignIn.setOnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            authViewModel.handleSignIn(email, password) {
                startActivity(intent)
               // Toast.makeText(requireContext(), "signed in", Toast.LENGTH_SHORT).show()
            }

        }
        return binding.root
    }

}