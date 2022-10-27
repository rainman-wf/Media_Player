package ru.netology.mediaplayer.adapter

import androidx.recyclerview.widget.RecyclerView
import ru.netology.mediaplayer.databinding.ItemBinding
import ru.netology.mediaplayer.model.TrackModel

class TrackViewHolder(
    private val binding: ItemBinding,
    private val onPlayClickListener: OnPlayClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(trackModel: TrackModel, selected: Boolean) {
        binding.apply {

            itemTitle.text = trackModel.fileName
            itemDuration.text = trackModel.duration

            val color = (if (selected) 0xFFBBDEFB else 0xFFFFFFFF ).toInt()
            rootLayout.setBackgroundColor(color)

            rootLayout.setOnClickListener { onPlayClickListener(trackModel) }
        }
    }
}