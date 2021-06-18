package com.chaubacho.themusicapp.data

import com.chaubacho.themusicapp.data.model.Song
import com.chaubacho.themusicapp.data.source.SongDataSource
import com.chaubacho.themusicapp.data.source.local.utils.OnDataLocalCallback

class SongRepository private constructor(private val local: SongDataSource) :
    SongDataSource {

    override fun getAllSong(callback: OnDataLocalCallback<List<Song>>) {
        local.getAllSong(callback)
    }

    companion object {
        private var instance: SongRepository? = null
        fun getInstance(local: SongDataSource) =
            instance ?: SongRepository(local).also { instance = it }
    }
}
