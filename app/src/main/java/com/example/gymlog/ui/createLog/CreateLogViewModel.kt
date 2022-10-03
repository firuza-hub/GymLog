package com.example.gymlog.ui.createLog

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.gymlog.base.BaseViewModel
import com.example.gymlog.data.models.WorkoutLog
import com.example.gymlog.ui.auth.signIn.SignInViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class CreateLogViewModel(application: Application): BaseViewModel(application) {

    private val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
    private val TAG = "CREATE_LOG_VIEWMODEL"
    private val db = Firebase.firestore

    var workoutType:String = ""
    var workoutSubtype: String = ""
    var notes: String = ""
    var picture: String = ""

    val _date = MutableLiveData<String>()
    val date: LiveData<String>
          get() = _date

    private val gymLogs = db.collection("gymLogs")

    init {
        _date.value =  formatter.format(Date())
        Log.i(TAG, formatter.format(Date()))
    }



    fun saveLog(redirect: () -> Unit) {

        if(!validateInputs()) return
        val log = WorkoutLog(null,
             workoutType, workoutSubtype, notes, picture, date.value!!
        )

        gymLogs.add(log)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                redirect()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
            }
    }

    private fun validateInputs(): Boolean {
        var errors = mutableListOf<String>()
        if( workoutType.isEmpty()) {
            errors.add("Workout type cannot be empty")
        }
        if( workoutSubtype.isEmpty()) {
            errors.add("Workout subtype cannot be empty")
        }
        _validationError.value = errors
        return errors.size == 0
    }

    class Factory(private val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CreateLogViewModel(app) as T
        }
    }

}