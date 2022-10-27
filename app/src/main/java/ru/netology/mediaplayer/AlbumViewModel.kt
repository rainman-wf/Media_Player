package ru.netology.mediaplayer

import android.icu.text.DecimalFormat
import android.media.MediaPlayer
import androidx.lifecycle.*
import ru.netology.mediaplayer.model.AlbumModel
import ru.netology.mediaplayer.model.TrackListState
import ru.netology.mediaplayer.model.TrackModel
import ru.netology.mediaplayer.utils.toModel
import kotlin.concurrent.thread

class AlbumViewModel : ViewModel(), DefaultLifecycleObserver {

    companion object {
        const val url =
            "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/"
    }

    private val remoteDataSource = RemoteDataSource()

    private val mediaPlayer = MediaPlayer().apply {
        setOnCompletionListener { onNextClicked() }
    }

    override fun onPause(owner: LifecycleOwner) {
        mediaPlayer.pause()
    }

    override fun onStop(owner: LifecycleOwner) {
        mediaPlayer.release()
    }

    private val _albumLiveData = MutableLiveData<AlbumModel>()
    val albumLiveData: LiveData<AlbumModel> = _albumLiveData

    private val _trackModel = MutableLiveData<TrackModel>()
    val trackModel: LiveData<TrackModel> = _trackModel

    val onPlayClickEvent = SingleLiveEvent<Unit>()
    val trackListState = SingleLiveEvent<TrackListState>()


    init {
        thread {
            trackListState.postValue(TrackListState.LOADING)
            val albumModel = remoteDataSource.getAlbum().toModel()
            _albumLiveData.postValue(albumModel)
            _trackModel.postValue(albumModel.tracks.first())
            trackListState.postValue(TrackListState.READY)
        }
    }

    fun onNextClicked() {
        trackModel.value?.next?.let {
            setTrackModel(it)
        }
    }

    fun onPrevClicked() {
        trackModel.value?.prev?.let {
            setTrackModel(it)
        }
    }

    fun onItemClicked(id: Int) {
        trackModel.value?.id?.let { currentTrackId ->
            if (currentTrackId != id) {
                setTrackModel(id)
            }
        }
    }

    private fun setTrackModel(id: Int) {
        _trackModel.value  = albumLiveData.value?.tracks?.find { it.id == id }
    }

    fun prepareTrack() {
        trackModel.value?.let {
            mediaPlayer.reset()
            mediaPlayer.setDataSource("$url${it.fileName}")
            mediaPlayer.prepare()
        }
    }

    fun onPlayClicked() {
        if (mediaPlayer.isPlaying) mediaPlayer.pause() else mediaPlayer.start()
        onPlayClickEvent.postValue(Unit)
    }

    fun isPlaying() = mediaPlayer.isPlaying

    fun getCurrentPosition(): String {
        val seconds = mediaPlayer.currentPosition / 1000
        val durationMinutes = seconds / 60
        val durationSeconds = DecimalFormat("00").format(seconds % 60)
        return "$durationMinutes:$durationSeconds"
    }

    fun getProgress(): Int {
        return (mediaPlayer.currentPosition.toDouble() / mediaPlayer.duration * 100).toInt()
    }

    fun setTrackPosition(progress: Int) {
        mediaPlayer.seekTo(mediaPlayer.duration * progress / 100)
    }
}