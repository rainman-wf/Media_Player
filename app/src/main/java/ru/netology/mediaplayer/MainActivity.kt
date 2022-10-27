package ru.netology.mediaplayer

import android.os.Bundle
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import ru.netology.mediaplayer.adapter.OnPlayClickListener
import ru.netology.mediaplayer.adapter.TrackAdapter
import ru.netology.mediaplayer.databinding.ActivityMainBinding
import ru.netology.mediaplayer.model.TrackListState
import ru.netology.mediaplayer.model.TrackModel

class MainActivity : AppCompatActivity() {

    private val viewModel: AlbumViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val trackListAdapter = TrackAdapter(
            object : OnPlayClickListener {
                override fun invoke(trackModel: TrackModel) {
                    viewModel.onItemClicked(trackModel.id)
                }
            }
        )

        binding.trackList.adapter = trackListAdapter

        viewModel.trackModel.observe(this) { track ->
            binding.player.title.text = track.fileName
            trackListAdapter.setSingleSelection(trackListAdapter.currentList.indexOf(track))
            viewModel.prepareTrack()
            viewModel.onPlayClicked()
        }

        viewModel.trackListState.observe(this) { trackState ->
            trackState?.let {
                when (it) {
                    TrackListState.READY -> {
                        binding.trackList.isVisible = true
                        binding.dataLoadingProgress.isVisible = false
                    }
                    TrackListState.LOADING -> {
                        binding.trackList.isVisible = false
                        binding.dataLoadingProgress.isVisible = true
                    }
                }
            }
        }

        viewModel.onPlayClickEvent.observe(this) {
            binding.player.playerPlay.setImageResource(
                if (viewModel.isPlaying()) R.drawable.ic_baseline_pause_circle_24 else R.drawable.ic_baseline_play_circle_24
            )

            lifecycleScope.launchWhenCreated {
                while (viewModel.isPlaying()) {
                    delay(1000)
                    binding.player.playerDuration.text = viewModel.getCurrentPosition()
                    binding.player.progressBar.setProgress(viewModel.getProgress(), true)
                }
            }
        }

        viewModel.albumLiveData.observe(this) { album ->

            binding.albumTitle.text = album.title
            binding.genre.text = album.genre
            binding.artist.text = album.artist

            trackListAdapter.submitList(album.tracks)
        }

        binding.player.playerPlay.setOnClickListener { viewModel.onPlayClicked() }
        binding.player.next.setOnClickListener { viewModel.onNextClicked() }
        binding.player.previous.setOnClickListener { viewModel.onPrevClicked() }
        binding.player.progressBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

            private var mProcess = 0

            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (fromUser) mProcess = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                println(mProcess)
                seekBar.progress = mProcess
                viewModel.setTrackPosition(mProcess)
            }
        })
    }
}