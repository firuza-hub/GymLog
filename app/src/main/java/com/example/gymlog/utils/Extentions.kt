package com.example.gymlog.utils

fun List<String>.toMessage(): String {
    var result = ""
    for (i in this) {
      result += "$i\n"
    }
    return result
}