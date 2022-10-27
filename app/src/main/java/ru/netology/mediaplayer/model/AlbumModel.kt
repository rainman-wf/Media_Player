package ru.netology.mediaplayer.model

data class AlbumModel(
    val id: Int,
    val title: String,
    val subtitle: String,
    val artist: String,
    val published: String,
    val genre: String,
    val tracks: List<TrackModel>
)
