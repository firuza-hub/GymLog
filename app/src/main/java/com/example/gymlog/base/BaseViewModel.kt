package com.example.gymlog.base

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth

abstract class BaseViewModel(app: Application): AndroidViewModel(app) {

    protected val firebaseAuth = FirebaseAuth.getInstance()
    protected val _validationError = MutableLiveData<List<String>>()
    val validationError: LiveData<List<String>>
        get() = _validationError

    init {
        _validationError.value = mutableListOf()
    }
}