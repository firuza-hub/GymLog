package com.example.gymlog.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.gymlog.R
import com.example.gymlog.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {


    private val authViewModel: AuthenticationViewModel by viewModels()

    private lateinit var binding: FragmentSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)
        binding.tvSubFooter.setOnClickListener{
            Navigation.findNavController(binding.root).navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }
        return binding.root
    }

}