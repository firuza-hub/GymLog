package com.example.gymlog.data.models

import java.util.*

data class WorkoutLog(
    val workoutType: String,
    val workoutSubtype: String,
    val notes: String,
    val picture: String,
    val date: String
)