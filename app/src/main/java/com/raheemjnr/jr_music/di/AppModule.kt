package com.raheemjnr.jr_music.di

import android.content.Context
import com.raheemjnr.jr_music.BaseApp
import com.raheemjnr.jr_music.media.MusicServiceConnection
import com.raheemjnr.jr_music.media.MusicSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AnalyticsModule {


    // graph for music service connection
    @Singleton
    @Provides
    fun provideMusicServiceConnection(
        @ApplicationContext context: Context,
        musicSource: MusicSource
    ) = MusicServiceConnection(context, musicSource)

    // // graph for music source
    @Singleton
    @Provides
    fun provideMusicSource(
        @ApplicationContext context: Context
    ) = MusicSource(context)


    //
    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): BaseApp {
        return app as BaseApp
    }


}