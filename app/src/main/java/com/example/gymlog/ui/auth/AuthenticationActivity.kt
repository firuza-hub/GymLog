package com.example.gymlog.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gymlog.R
import com.example.gymlog.ui.MainActivity
import com.google.android.material.snackbar.Snackbar

class AuthenticationActivity : AppCompatActivity() {
    private val authViewModel: AuthenticationViewModel by viewModels{AuthenticationViewModel.Factory(application)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)


        authViewModel.authenticationState.observe(this) {
            if (it == AuthenticationViewModel.AuthState.AUTHENTICATED) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}