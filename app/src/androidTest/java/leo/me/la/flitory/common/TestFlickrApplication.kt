package leo.me.la.flitory.common

import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import leo.me.la.flitory.FlickrApplication

internal class TestFlickrApplication : FlickrApplication() {

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerTestApplicationComponent.create()
    }
}
