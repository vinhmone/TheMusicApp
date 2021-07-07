package com.chaubacho.themusicapp.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.chaubacho.themusicapp.ui.notification.SongNotification.Companion.NEXT
import com.chaubacho.themusicapp.ui.notification.SongNotification.Companion.PLAY_PAUSE
import com.chaubacho.themusicapp.ui.notification.SongNotification.Companion.PREVIOUS
import com.chaubacho.themusicapp.ui.notification.SongNotificationCallback

class SongBroadcast(
    private val songCallback: SongNotificationCallback?
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            NEXT -> songCallback?.onNotificationNextSong()
            PLAY_PAUSE -> songCallback?.onNotificationPlayPauseSong()
            PREVIOUS -> songCallback?.onNotificationPreviousSong()
        }
    }
}
