package com.chaubacho.themusicapp.utils

import android.content.ContentResolver
import android.util.Log
import com.chaubacho.themusicapp.data.SongRepository
import com.chaubacho.themusicapp.data.source.local.SongLocalDataSource

object RepositoryUtil {

    fun getSongRepository(contentResolver: ContentResolver): SongRepository {
        val local = SongLocalDataSource.getInstance(contentResolver)
        return SongRepository.getInstance(local)
    }
}
