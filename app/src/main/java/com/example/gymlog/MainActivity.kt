package com.example.gymlog.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.gymlog.R
import com.example.gymlog.ui.auth.AuthenticationActivity
import com.example.gymlog.ui.auth.AuthenticationViewModel

class MainActivity : AppCompatActivity() {

    private val authViewModel: AuthenticationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authViewModel.authenticationState.observe(this) {
            if (it == AuthenticationViewModel.AuthState.UNAUTHENTICATED) {
                val intent = Intent(this, AuthenticationActivity::class.java)
                startActivity(intent)
            }
        }
    }
}


