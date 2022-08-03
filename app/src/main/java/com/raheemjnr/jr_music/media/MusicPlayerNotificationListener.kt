package com.raheemjnr.jr_music.media

import android.app.Notification
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager

/**
 * Listen for notification events.
 */
class MusicPlayerNotificationListener(private val jrPlayerService: JrPlayerService) :
    PlayerNotificationManager.NotificationListener {
    override fun onNotificationPosted(
        notificationId: Int,
        notification: Notification,
        ongoing: Boolean
    ) {
        jrPlayerService.apply {
            if (ongoing && !isForegroundService) {
                ContextCompat.startForegroundService(
                    applicationContext,
                    Intent(
                        applicationContext, this::class.java
                    )
                )
                startForeground(notificationId, notification)
                isForegroundService = true
            }
        }

    }

    override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
        jrPlayerService.apply {
            stopForeground(true)
            isForegroundService = false
            stopSelf()
        }

    }
}
