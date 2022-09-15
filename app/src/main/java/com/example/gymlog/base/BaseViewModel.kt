package com.example.gymlog.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

abstract class BaseViewModel(app: Application): AndroidViewModel(app) {

    protected val fb = FirebaseAuth.getInstance()
    protected val _validationError = MutableLiveData<String>()
    val validationError: LiveData<String>
        get() = _validationError
}