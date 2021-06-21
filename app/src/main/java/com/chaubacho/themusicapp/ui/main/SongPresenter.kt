package com.chaubacho.themusicapp.ui.main

import com.chaubacho.themusicapp.data.SongRepository
import com.chaubacho.themusicapp.data.model.Song
import com.chaubacho.themusicapp.data.source.local.utils.OnDataLocalCallback
import java.lang.Exception

class SongPresenter(
    private val view: SongContract.View,
    private val songRepository: SongRepository,
) : SongContract.Presenter {

    override fun getAllSongs() {
        songRepository.getAllSong(object : OnDataLocalCallback<List<Song>> {
            override fun onSucceed(data: List<Song>) {
                view.showAllSongs(data)
            }

            override fun onFailed(e: Exception?) {
                view.showError(e?.message.toString())
            }
        })
    }

    override fun start() {
        getAllSongs()
    }
}
