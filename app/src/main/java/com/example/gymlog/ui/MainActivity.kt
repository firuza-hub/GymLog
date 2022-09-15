package com.example.gymlog.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.example.gymlog.R
import com.example.gymlog.databinding.ActivityMainBinding
import com.example.gymlog.ui.auth.AuthenticationActivity
import com.example.gymlog.ui.auth.AuthenticationViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val authViewModel: AuthenticationViewModel by viewModels{AuthenticationViewModel.Factory(application)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        authViewModel.authenticationState.observe(this) {
            if (it == AuthenticationViewModel.AuthState.UNAUTHENTICATED) {
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
            }
        }

        binding.btnLogout.setOnClickListener { authViewModel.handleSignOut() }
    }
}


