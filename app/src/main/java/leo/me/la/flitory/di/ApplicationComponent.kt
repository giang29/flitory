package leo.me.la.flitory.di

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import leo.me.la.cache.di.CacheModule
import leo.me.la.common.di.Debug
import leo.me.la.data.di.DataModule
import leo.me.la.domain.di.DomainModule
import leo.me.la.flitory.FlickrApplication
import leo.me.la.remote.di.RemoteModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        SearchPhotoModule::class,
        CacheModule::class,
        RemoteModule::class,
        DataModule::class,
        DomainModule::class
    ])
internal interface ApplicationComponent : AndroidInjector<FlickrApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance @Debug debug: Boolean): ApplicationComponent
    }
}
