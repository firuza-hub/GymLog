package com.example.gymlog.ui.auth

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class AuthenticationViewModel : ViewModel() {

    val authenticationState = FirebaseUserLiveData().map { user ->
        if (user != null) {
            AuthState.AUTHENTICATED
        } else {
            AuthState.UNAUTHENTICATED
        }
    }


    fun handleSignIn(email: String, password: String) {
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->

                if (!task.isSuccessful) {
                    Log.i(TAG, "Login Failed with ${task.exception}")
                } else {
                    Log.i(TAG, "Login Successful")
                }
            }
    }

    fun handleSignOut() {
        FirebaseAuth.getInstance()
            .signOut()
    }

    fun handleSignUp(email: String, password: String, confirmPassword: String) {
        if (!isEmailValid(email)) {
            return
        }
        if (password != confirmPassword) {
            return
        }

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(
            email, password
        )
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.i(TAG, "Email signup is successful")
                } else {
                    task.exception?.let {
                        Log.i(TAG, "Email signup failed with error ${it.localizedMessage}")
                    }
                }
            }
    }


    enum class AuthState {
        AUTHENTICATED,
        UNAUTHENTICATED
    }

    private fun isEmailValid(email: String): Boolean {
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())
    }


}
