package com.example.gymlog.ui.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.gymlog.R
import com.example.gymlog.databinding.FragmentSignInBinding


class SignInFragment : Fragment() {

    private val authViewModel: AuthenticationViewModel by viewModels()

    private lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)
        binding.tvSubFooter.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
        }
        return binding.root
    }

}