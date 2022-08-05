package com.raheemjnr.jr_music.media

import android.net.Uri
import android.os.Bundle
import android.os.ResultReceiver
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ext.mediasession.MediaSessionConnector
import com.raheemjnr.jr_music.media.extentions.album
import com.raheemjnr.jr_music.media.extentions.trackNumber
import com.raheemjnr.jr_music.utils.TAG

class MediaPlaybackPreparer(
    private val musicSource: MusicSource,
    private val playerPrepared: (MediaMetadataCompat) -> Unit
) : MediaSessionConnector.PlaybackPreparer {

    /**
     * UAMP supports preparing (and playing) from search, as well as media ID, so those
     * capabilities are declared here.
     *
     * TODO: Add support for ACTION_PREPARE and ACTION_PLAY, which mean "prepare/play something".
     */
    override fun getSupportedPrepareActions(): Long =
        PlaybackStateCompat.ACTION_PREPARE_FROM_MEDIA_ID or
                PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID

    override fun onPrepare(playWhenReady: Boolean) {
        /*
        val recentSong = storage.loadRecentSong() ?: return
        onPrepareFromMediaId(
        recentSong.mediaId!!,
        playWhenReady,
        recentSong.description.extras
        )
        */
        musicSource.songs[0].description.mediaId?.let { mediaId ->
            onPrepareFromMediaId(
                mediaId,
                playWhenReady,
                musicSource.songs[0].bundle
            )
        }

    }

    override fun onPrepareFromMediaId(
        mediaId: String,
        playWhenReady: Boolean,
        extras: Bundle?
    ) {
        musicSource.whenReady {
            val itemToPlay: MediaMetadataCompat? = musicSource.songs.find { item ->
                item.description.mediaId == mediaId
            }
            if (itemToPlay == null) {
                Log.w(TAG, "Content not found: MediaID=$mediaId")
                // TODO: Notify caller of the error.
            } else {
                playerPrepared(itemToPlay)
//                val playbackStartPositionMs =
//                    extras?.getLong(
//                        MEDIA_DESCRIPTION_EXTRAS_START_PLAYBACK_POSITION_MS,
//                        C.TIME_UNSET
//                    )
//                        ?: C.TIME_UNSET
//
//                preparePlaylist(
//                    buildPlaylist(itemToPlay),
//                    itemToPlay,
//                    playWhenReady,
//                    playbackStartPositionMs
//                )
            }
//            musicSource.whenReady {
//                val itemToPlay = musicSource.songs.find { mediaId == it.description.mediaId }
//                playerPrepared(itemToPlay)
//            }
        }
    }

    override fun onPrepareFromSearch(query: String, playWhenReady: Boolean, extras: Bundle?) = Unit

    override fun onPrepareFromUri(uri: Uri, playWhenReady: Boolean, extras: Bundle?) = Unit

    override fun onCommand(
        player: Player,
        command: String,
        extras: Bundle?,
        cb: ResultReceiver?
    ) = false

    /**
     * Builds a playlist based on a [MediaMetadataCompat].
     * @param item Item to base the playlist on.
     * @return a [List] of [MediaMetadataCompat] objects representing a playlist.
     */
    private fun buildPlaylist(item: MediaMetadataCompat): List<MediaMetadataCompat> =
        musicSource.songs.filter { it.album == item.album }.sortedBy { it.trackNumber }
}