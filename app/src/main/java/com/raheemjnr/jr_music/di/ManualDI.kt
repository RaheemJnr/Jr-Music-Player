package com.raheemjnr.jr_music.di

import android.app.Activity
import android.content.Context
import com.raheemjnr.jr_music.media.MusicServiceConnection
import com.raheemjnr.jr_music.media.MusicSource
import com.raheemjnr.jr_music.ui.viewmodels.MainViewModel

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

    fun provideMainViewModel(context: Context)
            : MainViewModel.Factory {
        val applicationContext = context as Activity
        val musicServiceConnection = provideMusicServiceConnection(applicationContext)
        return MainViewModel.Factory(context.application, musicServiceConnection)
    }

//    private val viewModel: NewsViewModel by lazy {
//        val activity = requireNotNull(this.activity) {
//            "You can only access the viewModel after onActivityCreated()"
//        }
//        ViewModelProviders.of(this, NewsViewModel.Factory(activity.application))
//            .get(NewsViewModel::class.java)
//    }
}