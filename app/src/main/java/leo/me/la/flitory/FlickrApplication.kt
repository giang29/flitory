package leo.me.la.flitory

import android.app.Application
import leo.me.la.data.dataModule
import leo.me.la.domain.domainModule
import leo.me.la.presentation.presentationModule
import leo.me.la.remote.remoteModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

internal class FlickrApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        // Start Koin
        startKoin {
            androidContext(this@FlickrApplication)
            modules(
                appModule,
                domainModule,
                dataModule,
                presentationModule,
                remoteModule
            )
        }
    }
}
