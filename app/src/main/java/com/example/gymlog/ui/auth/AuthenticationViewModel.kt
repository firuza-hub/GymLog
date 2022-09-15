package com.example.gymlog.ui.auth

import android.app.Application
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import androidx.lifecycle.*
import com.example.gymlog.R
import com.example.gymlog.base.BaseViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class AuthenticationViewModel(app: Application) : BaseViewModel(app) {

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthState.AUTHENTICATED
        } else {
            AuthState.UNAUTHENTICATED
        }
    }

    fun handleSignOut() {
        fb.signOut()
    }

    enum class AuthState {
        AUTHENTICATED,
        UNAUTHENTICATED
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return AuthenticationViewModel(app) as T
        }
    }
}
