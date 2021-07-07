package com.chaubacho.themusicapp.ui.main

import com.chaubacho.themusicapp.base.BasePresenter
import com.chaubacho.themusicapp.data.model.Song

interface SongContract {

    interface View {
        fun showAllSongs(songs: List<Song>)
        fun showError(e: String)
    }

    interface Presenter : BasePresenter {
        fun getAllSongs()
    }
}
