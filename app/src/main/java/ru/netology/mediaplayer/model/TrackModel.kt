package ru.netology.mediaplayer.model

data class TrackModel(
    val id: Int,
    val next: Int? = null,
    val prev: Int? = null,
    val fileName: String,
    val duration: String
)
