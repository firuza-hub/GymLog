package com.example.gymlog.ui.calendar

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gymlog.base.BaseViewModel
import com.example.gymlog.data.models.WorkoutLog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LogListViewModel(app: Application) : BaseViewModel(app) {
    private val TAG = "LOG_LIST_VIEWMODEL"
    private val db = Firebase.firestore
    private val gymLogs = db.collection("gymLogs")

    val _logs = MutableLiveData<MutableList<WorkoutLog>>()
    val logs: LiveData<MutableList<WorkoutLog>>
        get() = _logs


    init {
        fetchLogs()
        Log.i(TAG, "Gym logs have been fetched")
    }

    fun fetchLogs() {
        gymLogs.get().addOnSuccessListener {

            Log.i(TAG, "Gym logs have been fetched")
            _logs.value = it.toObjects<WorkoutLog>(WorkoutLog::class.java)
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LogListViewModel(app) as T
        }
    }
}