package com.example.gymlog.data.models

import com.google.firebase.firestore.DocumentId

data class WorkoutLog(
    @DocumentId
    val id: String? = null,
    val workoutType: String,
    val workoutSubtype: String,
    val notes: String,
    val pictureId: String,
    val date: String
){
    constructor(): this(null, "", "", "", "", "")
}