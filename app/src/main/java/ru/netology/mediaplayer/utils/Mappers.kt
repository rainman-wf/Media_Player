package ru.netology.mediaplayer.utils

import android.media.MediaMetadataRetriever
import ru.netology.mediaplayer.AlbumViewModel
import ru.netology.mediaplayer.model.Album
import ru.netology.mediaplayer.model.AlbumModel
import ru.netology.mediaplayer.model.Track
import ru.netology.mediaplayer.model.TrackModel

fun Album.toModel() = AlbumModel(
    id,
    title,
    subtitle,
    artist,
    published,
    genre,
    tracks.toTrackList(),
)

fun List<Track>.toTrackList() : List<TrackModel> {

    val trackList = mutableListOf<TrackModel>()

    forEachIndexed { index, track ->

        val metadataRetriever = MediaMetadataRetriever()
        metadataRetriever.setDataSource("${AlbumViewModel.url}${track.file}")


        trackList.add(

            TrackModel(
                id = track.id,
                next = if (index == lastIndex) this.first().id else this[index + 1].id,
                prev = if (index == 0) this.last().id else this[index - 1].id,
                fileName = track.file,
                duration = getDuration(
                    metadataRetriever.extractMetadata(
                        MediaMetadataRetriever.METADATA_KEY_DURATION
                    )?.toInt() ?: 0
                )
            )
        )
    }
    return trackList
}