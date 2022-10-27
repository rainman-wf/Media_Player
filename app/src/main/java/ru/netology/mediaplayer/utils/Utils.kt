package ru.netology.mediaplayer.utils

import android.icu.text.DecimalFormat

fun getDuration(millis: Int): String {
    val seconds = millis / 1000
    val durationMinutes = seconds / 60
    val durationSeconds = DecimalFormat("00").format(seconds % 60)
    return "$durationMinutes:$durationSeconds"
}