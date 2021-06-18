package com.chaubacho.themusicapp.ui.main

import com.chaubacho.themusicapp.base.BasePresenter
import com.chaubacho.themusicapp.data.model.Song
import java.lang.Exception

interface SongContract {

    interface View {
        fun showAllSongs(songs: List<Song>)
        fun showError(e: Exception?)
    }

    interface Presenter : BasePresenter {
        fun getAllSongs()
    }
}
