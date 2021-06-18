package com.chaubacho.themusicapp.ui.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.chaubacho.themusicapp.data.model.Song
import kotlinx.android.synthetic.main.item_song.view.*

class SongViewHolder(
    itemView: View,
    private var onItemClick: (Song) -> Unit,
) : RecyclerView.ViewHolder(itemView) {

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
        itemView.apply {
            textSongName.text = item.name
            textSongArtist.text = item.artist
        }
    }
}
