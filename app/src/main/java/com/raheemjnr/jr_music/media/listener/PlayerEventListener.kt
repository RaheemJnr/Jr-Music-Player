package com.raheemjnr.jr_music.media

import android.support.v4.media.MediaMetadataCompat
import android.util.Log
import android.widget.Toast
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.util.Util
import com.raheemjnr.jr_music.R
import com.raheemjnr.jr_music.utils.TAG

/**
 * Listen for events from ExoPlayer.
 */
class MusicPlayerEventListener(
    private val musicService: JrPlayerService,
    private val notificationManager: MusicNotificationManager,
    private val currentPlayer: ExoPlayer,
    private val currentPlaylistItems: List<MediaMetadataCompat>,
    private var currentMediaItemIndex: Int = 0,
) : Player.Listener {
    private var isForegroundService = false

    @Deprecated("Deprecated in Java")
    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        super.onPlayWhenReadyChanged(playWhenReady, playbackState)
        when (playbackState) {
            Player.STATE_BUFFERING,
            Player.STATE_READY -> {
                notificationManager.showNotificationForPlayer(currentPlayer)
                if (playbackState == Player.STATE_READY) {

                    // When playing/paused save the current media item in persistent
                    // storage so that playback can be resumed between device reboots.
                    // Search for "media resumption" for more information.
                    // saveRecentSongToStorage()

                    if (!playWhenReady) {
                        // If playback is paused we remove the foreground state which allows the
                        // notification to be dismissed. An alternative would be to provide a
                        // "close" button in the notification which stops playback and clears
                        // the notification.
                        musicService.stopForeground(false)
                        isForegroundService = false
                    }
                }
            }
            else -> {
                notificationManager.hideNotification()
            }
        }
    }

    override fun onEvents(player: Player, events: Player.Events) {
        if (events.contains(Player.EVENT_POSITION_DISCONTINUITY)
            || events.contains(Player.EVENT_MEDIA_ITEM_TRANSITION)
            || events.contains(Player.EVENT_PLAY_WHEN_READY_CHANGED)
        ) {
            currentMediaItemIndex = if (currentPlaylistItems.isNotEmpty()) {
                Util.constrainValue(
                    player.currentMediaItemIndex,
                    /* min= */ 0,
                    /* max= */ currentPlaylistItems.size - 1
                )
            } else 0
        }
    }

    override fun onPlayerError(error: PlaybackException) {
        var message = R.string.generic_error;
        Log.e(TAG, "Player error: " + error.errorCodeName + " (" + error.errorCode + ")");
        if (error.errorCode == PlaybackException.ERROR_CODE_IO_FILE_NOT_FOUND) {
            message = R.string.error_media_not_found;
        }
        Toast.makeText(
            musicService,
            message,
            Toast.LENGTH_LONG
        ).show()
    }
}
