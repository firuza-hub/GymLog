package com.example.gymlog.ui.auth.signUp

import android.app.Application
import android.content.ContentValues
import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gymlog.R
import com.example.gymlog.base.BaseViewModel
import com.google.firebase.auth.UserProfileChangeRequest

class SignUpViewModel(app:Application): BaseViewModel(app) {

    private var _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    private var _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String>
        get() = _passwordError


    private var _nameError = MutableLiveData<String>()
    val nameError: LiveData<String>
        get() = _nameError

    init {
        _emailError.value = ""
        _passwordError.value = ""
        _nameError.value = ""
    }


    fun handleSignUp(email: String, password: String, name: String, redirect: () -> Unit) {
        resetErrors()
        if (signUpInputHasErrors(email, password, name)) return

        firebaseAuth.createUserWithEmailAndPassword(
            email, password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                firebaseAuth.currentUser?.updateProfile(
                    UserProfileChangeRequest.Builder().setDisplayName(name).build()
                )
                Log.i(ContentValues.TAG, "Email signup is successful")
                redirect()
            } else {
                task.exception?.let {
                    Log.i(ContentValues.TAG, "Email signup failed with error ${it.localizedMessage}")
                }
            }
        }
    }

    fun resetErrors(view: View? = null) {
        when (view?.id) {
            R.id.etName -> _nameError.value = ""
            R.id.etPassword -> _passwordError.value = ""
            R.id.etEmail -> _emailError.value = ""
            else -> {
                _nameError.value = ""
                _passwordError.value = ""
                _emailError.value = ""
            }
        }
    }

    private fun signUpInputHasErrors(email: String, password: String, name: String): Boolean {
        var result = false
        if (!isEmailValid(email)) {
            _emailError.value = "Email address format is not valid"
            result = true
        }
        if (!isPasswordValid(password)) {
            _passwordError.value = "Password should consist of at least 5 symbols"
            result = true
        }
        if (!isUserNameValid(name)) {
            _nameError.value = "Password should consist of at least 5 symbols"
            result = true
        }
        return result
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    private fun isEmailValid(email: String): Boolean {
        return email.matches("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+".toRegex())
    }

    private fun isUserNameValid(name: String): Boolean {
        return name.isNotEmpty()
    }


    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SignUpViewModel(app) as T
        }
    }
}