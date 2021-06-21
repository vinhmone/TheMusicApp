package com.chaubacho.themusicapp.data.source

import com.chaubacho.themusicapp.data.model.Song
import com.chaubacho.themusicapp.data.source.local.utils.OnDataLocalCallback

interface SongDataSource {
    fun getAllSong(callback: OnDataLocalCallback<List<Song>>)
}
