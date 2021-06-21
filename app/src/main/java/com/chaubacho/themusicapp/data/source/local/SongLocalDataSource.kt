package com.chaubacho.themusicapp.data.source.local

import android.content.ContentResolver
import android.provider.MediaStore
import com.chaubacho.themusicapp.data.model.Song
import com.chaubacho.themusicapp.data.source.SongDataSource
import com.chaubacho.themusicapp.data.source.local.utils.LocalAsyncTask
import com.chaubacho.themusicapp.data.source.local.utils.OnDataLocalCallback

class SongLocalDataSource private constructor(
    private val contentResolver: ContentResolver,
) : SongDataSource {

    override fun getAllSong(callback: OnDataLocalCallback<List<Song>>) {
        LocalAsyncTask<Unit, List<Song>>(callback) {
            getSongFromDevice()
        }.execute(Unit)
    }

    private fun getSongFromDevice(): List<Song> {
        val songs = mutableListOf<Song>()
        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.ArtistColumns.ARTIST
        )
        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()) {
                songs.add(Song(it))
            }
        }
        cursor?.close()
        return songs
    }

    companion object {
        private var instance: SongLocalDataSource? = null
        fun getInstance(contentResolver: ContentResolver) =
            instance ?: SongLocalDataSource(contentResolver).also { instance = it }
    }
}
