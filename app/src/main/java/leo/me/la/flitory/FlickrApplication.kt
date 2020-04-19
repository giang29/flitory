package leo.me.la.flitory

import android.app.Activity
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.DispatchingAndroidInjector
import leo.me.la.flitory.di.DaggerApplicationComponent
import javax.inject.Inject

internal open class FlickrApplication: DaggerApplication() {
    @Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(BuildConfig.DEBUG)
    }
}
