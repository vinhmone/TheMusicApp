package com.chaubacho.themusicapp.data.model

import android.database.Cursor
import android.provider.MediaStore

data class Song(
    var id: String,
    var name: String,
    var artist: String
) {
    constructor(cursor: Cursor) : this(
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)),
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE)),
        cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.ArtistColumns.ARTIST))
    )
}
