package com.message.toschat

import android.app.Application
import com.message.toschat.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

class MainApp: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@MainApp)
            androidLogger(Level.DEBUG)
            androidFileProperties()
            module {
                listOf(appModules)
            }
        }

    }
}