package ru.netology.mediaplayer.adapter

import androidx.recyclerview.widget.DiffUtil
import ru.netology.mediaplayer.model.TrackModel

class TrackDiffCallBack: DiffUtil.ItemCallback<TrackModel>() {
    override fun areItemsTheSame(oldItem: TrackModel, newItem: TrackModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: TrackModel, newItem: TrackModel): Boolean {
        return oldItem == newItem
    }
}