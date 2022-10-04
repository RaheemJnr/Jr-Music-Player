package com.raheemjnr.jr_music.di

import android.content.Context
import com.raheemjnr.jr_music.media.MusicServiceConnection
import com.raheemjnr.jr_music.media.MusicSource

//interface DIComponent {
//    val musicSource: MusicSource
//}
//
//
//@SuppressLint("StaticFieldLeak")
//object DiModule : DIComponent {
//    //music source
//    override val musicSource: MusicSource = MusicSource(App.appContext)
//
//}
//
//fun musicSource(): DIComponent = DiModule


object InjectorUtils {
    fun provideMusicSource(context: Context): MusicSource {
        return MusicSource(context)
    }

    fun provideMusicServiceConnection(context: Context): MusicServiceConnection {
        return MusicServiceConnection.getInstance(context, provideMusicSource(context))
    }
}