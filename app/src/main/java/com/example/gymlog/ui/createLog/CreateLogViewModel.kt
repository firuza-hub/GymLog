package com.example.gymlog.ui.createLog

import android.app.Application
import android.graphics.Bitmap
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.*
import com.example.gymlog.base.BaseViewModel
import com.example.gymlog.data.models.WorkoutLog
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class CreateLogViewModel(application: Application) : BaseViewModel(application) {

    private val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm")
    private var storage = FirebaseStorage.getInstance()
    private val TAG = "CREATE_LOG_VIEWMODEL"
    private val db = Firebase.firestore

    var photoName: String = ""
    var bitMap: Bitmap? = null
    var workoutType: String = ""
    var workoutSubtype: String = ""
    var notes: String = ""
    var pictureId: String = ""

    val _date = MutableLiveData<String>()
    val date: LiveData<String>
        get() = _date

    private val _showLoader = MutableLiveData<Boolean>()
    val showLoader: LiveData<Boolean>
        get() = _showLoader

    private val gymLogs = db.collection("gymLogs")

    init {
        _showLoader.value = false
        _date.value = formatter.format(Date())
        Log.i(TAG, formatter.format(Date()))
    }


    fun saveLog(redirect: () -> Unit) {
        _showLoader.value = true
        if (!validateInputs()){
            _showLoader.value = false
            return
        }

        if (bitMap != null && photoName.isNotBlank()) {
            Log.d(TAG, "Picture Save Initiated")
            if (!savePic(bitMap!!, photoName)) {
                _showLoader.value = false
                return
            }
        }
        val log = WorkoutLog(
            null,
            workoutType, workoutSubtype, notes, pictureId, date.value!!
        )

        gymLogs.add(log)
            .addOnSuccessListener { documentReference ->
                Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                _showLoader.value = false
                redirect()
            }
            .addOnFailureListener { e ->
                Log.w(TAG, "Error adding document", e)
                _showLoader.value = false
            }

    }


    // Create a storage reference from our app
    private val storageRef = storage.getReferenceFromUrl("gs://gymlog-by-fsa.appspot.com");

    fun savePic(bitmap: Bitmap, imageName: String): Boolean = runBlocking {

        val mountainsRef = storageRef.child(imageName);

        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data: ByteArray = baos.toByteArray()

        val uploadTask = mountainsRef.putBytes(data)
        try {
            uploadTask.await()

            val uploadId = mountainsRef.downloadUrl.await()
            Log.i("MYCAMERA", "download url: $uploadId")
            pictureId = uploadId.toString()
            Toast.makeText(
                getApplication(),
                "File has been saved successfully!",
                Toast.LENGTH_SHORT
            ).show()

        } catch (ex: Exception) {
            Log.i("MYCAMERA", ex.message.toString())
            _validationError.value =
                mutableListOf("Something went wrong. Log could not be saved")
            return@runBlocking false
        }
        Log.i("MYCAMERA", "Storage upload success")

        return@runBlocking true
    }


    private fun validateInputs(): Boolean {
        var errors = mutableListOf<String>()
        if (workoutType.isEmpty()) {
            errors.add("Workout type cannot be empty")
        }
        if (workoutSubtype.isEmpty()) {
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