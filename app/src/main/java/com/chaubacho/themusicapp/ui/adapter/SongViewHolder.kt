package com.chaubacho.themusicapp.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chaubacho.themusicapp.data.model.Song
import com.chaubacho.themusicapp.databinding.ItemSongBinding

class SongViewHolder(
    itemView: View,
    private var onItemClick: (Song) -> Unit,
) : RecyclerView.ViewHolder(itemView) {

    private val binding = ItemSongBinding.bind(itemView)
    private var itemSong: Song? = null

    init {
        itemView.setOnClickListener {
            itemSong?.let {
                onItemClick(it)
            }
        }
    }

    fun bindData(item: Song) {
        itemSong = item
        binding.apply {
            textSongName.text = item.name
            textSongArtist.text = item.artist
        }
    }
}
