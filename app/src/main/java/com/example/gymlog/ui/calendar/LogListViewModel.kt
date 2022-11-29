package com.example.gymlog.ui.calendar

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.example.gymlog.base.BaseViewModel
import com.example.gymlog.data.models.WorkoutLog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class LogListViewModel(app: Application) : BaseViewModel(app) {
    private val TAG = "LOG_LIST_VIEWMODEL"
    private val db = Firebase.firestore
    private val gymLogs = db.collection("gymLogs")
    val listLoading: LiveData<Boolean>
        get() = _listLoading

    private val _listLoading = MutableLiveData<Boolean>()

    val _logs = MutableLiveData<MutableList<WorkoutLog>>()
    val logs: LiveData<MutableList<WorkoutLog>>
        get() = _logs

    private val storage = FirebaseStorage.getInstance()

    init {
        _listLoading.value = false
        fetchLogs()
        Log.i(TAG, "Gym logs have been fetched")
    }

    private fun fetchLogs() {
        _listLoading.value = true
        gymLogs.get().addOnSuccessListener {

            Log.i(TAG, "Gym logs have been fetched")
            _logs.value = it.toObjects<WorkoutLog>(WorkoutLog::class.java)
            _listLoading.value = false
        }
            .addOnFailureListener {
                Log.i(TAG, "Gym logs fetch error has occurred: $it")
                _listLoading.value = false
            }
    }

    fun deleteLog(id: String) {
        viewModelScope.launch {
            val pictureId = gymLogs.document(id).get().await().toObject<WorkoutLog>()?.pictureId
            gymLogs.document(id).delete().addOnSuccessListener {
                Log.i(TAG, "Log has been deleted: $id")
                if (!pictureId.isNullOrEmpty()) {
                    val fileRef = storage.getReferenceFromUrl(pictureId);
                    fileRef.delete().addOnSuccessListener {
                        Log.i(TAG, "Log picutre has been deleted: $pictureId")
                    }
                }
                //TODO: MOTION LAYOUT: AUTOMATE SWIPE TILL THE END AND BACK TO THE START
                //      SWIPE BACK ITEMS DELETE CARD IF ANOTHER ITEMS DELETE CARD HAS BEEN SWIPED
                fetchLogs()
            }
        }
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return LogListViewModel(app) as T
        }
    }
}