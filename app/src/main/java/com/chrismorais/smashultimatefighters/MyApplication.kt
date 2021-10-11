package com.chrismorais.smashultimatefighters

import android.app.Application
import com.chrismorais.smashultimatefighters.features.fighters.di.fightersModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

open class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@MyApplication)
            modules(listOf(fightersModule))
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}