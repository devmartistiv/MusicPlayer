package com.martist.musicplayer.presentation

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService

class PlayerService : MediaSessionService() {
    private lateinit var player: ExoPlayer
    private var session: MediaSession? = null
    override fun onCreate() {
        super.onCreate()

        player = ExoPlayer.Builder(this).build()
        session = MediaSession.Builder(this, player).build()

        player.addListener(object : Player.Listener {
            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                super.onMediaItemTransition(mediaItem, reason)

            }
        })

        player.prepare()
        player.duration
    }

    override fun onGetSession(p0: MediaSession.ControllerInfo): MediaSession? {
        return session
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
        session?.release()
    }
}