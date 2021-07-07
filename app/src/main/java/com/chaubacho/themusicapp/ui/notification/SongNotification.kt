package com.chaubacho.themusicapp.ui.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.chaubacho.themusicapp.R
import com.chaubacho.themusicapp.service.SongPlayerService

class SongNotification(private val service: SongPlayerService) {

    private var manage: NotificationManager? = null
    private var remoteView: RemoteViews? = null

    fun createNotification() {
        createChannel()
        val notification = NotificationCompat
            .Builder(service, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_audiotrack_24)
            .setCustomBigContentView(createRemoteView())
            .setSound(null)
            .setDefaults(0)
            .build()
        manage?.notify(NOTIFICATION_MUSIC_ID, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_ID,
                NotificationManager.IMPORTANCE_LOW
            )
            manage = service.getSystemService(NotificationManager::class.java)
            manage?.createNotificationChannel(channel)
        }
    }

    private fun createRemoteView(): RemoteViews? {
        remoteView = RemoteViews(service.packageName, R.layout.remote_view_song)
        remoteView?.setTextViewText(R.id.textRemoteSongInfo, service.getSongInfo())
        createPendingIntent(PLAY_PAUSE, R.id.imageRemotePlayPauseSong)
        createPendingIntent(NEXT, R.id.imageRemoteNextSong)
        createPendingIntent(PREVIOUS, R.id.imageRemotePreviousSong)
        return remoteView
    }

    private fun createPendingIntent(action: String, viewId: Int) {
        val pendingIntent =
            PendingIntent.getBroadcast(service, BROADCAST_ID, Intent(action), 0)
        remoteView?.setOnClickPendingIntent(viewId, pendingIntent)
    }

    companion object {
        const val CHANNEL_ID = "com.chaubacho.themusicapp"
        const val PLAY_PAUSE = "play"
        const val NEXT = "next"
        const val PREVIOUS = "previous"

        const val NOTIFICATION_MUSIC_ID = 200
        const val BROADCAST_ID = 100
    }
}
