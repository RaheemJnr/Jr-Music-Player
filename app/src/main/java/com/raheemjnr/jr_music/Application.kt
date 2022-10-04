package com.raheemjnr.jr_music

import android.app.Application
import android.content.Context

class App: Application() {
//    companion object {
//        private val applicationContext: Context? = null
//
//        @JvmStatic
//        fun applicationCxt() = applicationContext!!
//    }

    override fun onCreate() {
    super.onCreate()
    appContext = applicationContext
}

companion object {

    lateinit  var appContext: Context

}
}



//override fun onCreate() {
//    super.onCreate()
//    MyApplication.appContext = applicationContext
//}
//
//companion object {
//
//    lateinit  var appContext: Context
//
//}