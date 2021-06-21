package com.chaubacho.themusicapp.service

import android.app.Service
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.provider.MediaStore
import com.chaubacho.themusicapp.data.model.Song
import com.chaubacho.themusicapp.ui.notification.SongNotification
import com.chaubacho.themusicapp.ui.notification.SongNotification.Companion.FOREGROUND_ID
import java.lang.NumberFormatException

class SongPlayerService : Service() {

    private var mediaPlayer: MediaPlayer? = null
    private var songNotification: SongNotification? = null
    private var playingSong: Song? = null

    override fun onCreate() {
        super.onCreate()
        mediaPlayer = MediaPlayer()
        songNotification = SongNotification(this)
    }

    override fun onBind(intent: Intent?): IBinder {
        return SongBinder(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.release()
        mediaPlayer?.stop()
        this.stopSelf()
    }

    fun createMedia(song: Song) {
        playingSong = song
        mediaPlayer?.release()
        try {
            val songId = song.id.toLong()
            val uri = ContentUris.withAppendedId(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                songId
            )
            mediaPlayer = MediaPlayer.create(applicationContext, uri)
        } catch (e: NumberFormatException) {
            e.printStackTrace()
        }
    }

    fun play() {
        mediaPlayer?.start()
        startForeground(FOREGROUND_ID, songNotification?.createNotification())
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun seekTo(newPosition: Int) {
        mediaPlayer?.seekTo(newPosition)
    }

    fun getSongInfo() = "${playingSong?.name}"

    fun getDuration() = mediaPlayer?.duration

    fun getCurrentPosition() = mediaPlayer?.currentPosition

    fun isPlaying() = mediaPlayer?.isPlaying

    class SongBinder(private var service: SongPlayerService) : Binder() {
        fun getService(): SongPlayerService = service
    }

    companion object {
        fun getIntent(context: Context) = Intent(context, SongPlayerService::class.java)
    }
}
