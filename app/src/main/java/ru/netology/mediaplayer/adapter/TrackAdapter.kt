package ru.netology.mediaplayer.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.mediaplayer.databinding.ItemBinding
import ru.netology.mediaplayer.model.TrackModel

class TrackAdapter(
    private val onPlayClickListener: OnPlayClickListener
) : ListAdapter<TrackModel, TrackViewHolder>(TrackDiffCallBack()) {

    var selectedItem = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackViewHolder(binding, onPlayClickListener)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val trackModel = getItem(position)
        holder.bind(trackModel, position == selectedItem)
    }

    fun setSingleSelection(position: Int) {
        if (position == RecyclerView.NO_POSITION) return
        notifyItemChanged(selectedItem)
        selectedItem = position
        notifyItemChanged(selectedItem)
    }
}