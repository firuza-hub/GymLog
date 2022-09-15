package com.example.gymlog.utils

data class NetworkResult<T> (val data: T, val errorMessage: ErrorMessage)

data class ErrorMessage (val message:String, val type: ErrorType)

enum class ErrorType{
    Validation,
    Warning,
    Exception
}