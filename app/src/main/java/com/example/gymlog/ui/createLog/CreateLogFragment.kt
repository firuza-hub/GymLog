package com.example.gymlog.ui.createLog

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.example.gymlog.R
import com.example.gymlog.databinding.FragmentCreateLogBinding
import com.google.firebase.auth.FirebaseAuth

class CreateLogFragment : Fragment() {

    private lateinit var binding: FragmentCreateLogBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_create_log, container, false)
        return binding.root
    }

}