package com.example.gymlog.ui.auth

import android.app.Application
import androidx.lifecycle.*
import com.example.gymlog.base.BaseViewModel

class AuthenticationViewModel(app: Application) : BaseViewModel(app) {

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthState.AUTHENTICATED
        } else {
            AuthState.UNAUTHENTICATED
        }
    }

    fun handleSignOut() {
        firebaseAuth.signOut()
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
