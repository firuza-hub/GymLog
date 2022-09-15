package com.example.gymlog.ui.auth.signIn

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
import com.example.gymlog.ui.auth.AuthenticationViewModel
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class SignInViewModel(app:Application):BaseViewModel(app) {

    private var _emailError = MutableLiveData<String>()
    val emailError: LiveData<String>
        get() = _emailError

    private var _passwordError = MutableLiveData<String>()
    val passwordError: LiveData<String>
        get() = _passwordError


    init {
        _emailError.value = ""
        _passwordError.value = ""
    }

    fun handleSignIn(email: String, password: String, redirect: () -> Unit) {
        resetErrors()
        if (signInInputHasErrors(email, password)) return

        fb.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task: Task<AuthResult> ->

                if (task.isSuccessful) {
                    Log.i(ContentValues.TAG, "Login Successful")
                    redirect()
                } else {
                    task.exception!!.localizedMessage?.let {
                        _validationError.value = it// ErrorMessage(it, ErrorType.Validation)
                        Log.i(ContentValues.TAG, "Login Failed with: $it")
                    }

                }
            }
    }

    fun resetErrors(view: View? = null) {
        when (view?.id) {
            R.id.etPassword -> _passwordError.value = ""
            R.id.etEmail -> _emailError.value = ""
            else -> {
                _passwordError.value = ""
                _emailError.value = ""
            }
        }
    }

    private fun signInInputHasErrors(email: String?, password: String?): Boolean {
        var result = false
        if (email.isNullOrEmpty()) {
            _emailError.value = getApplication<Application>().getString(R.string.email_input_mandatory_validation)
            result = true
        }
        if (password.isNullOrEmpty()) {
            _passwordError.value = getApplication<Application>().getString(R.string.password_input_mandatory_validation)
            result = true
        }
        return result
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SignInViewModel(app) as T
        }
    }

}