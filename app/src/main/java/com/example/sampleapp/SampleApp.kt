package com.example.sampleapp
import android.app.Application
import com.example.sampleapp.injection.*
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class SampleApp : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@SampleApp)
            modules(
                listOf(
                    utilsModule,
                    databaseModule,
                    apiModule,
                    cacheModule,
                    repoModule,
                    useCaseModule,
                    viewModelsModule
                )
            )
        }
    }
}