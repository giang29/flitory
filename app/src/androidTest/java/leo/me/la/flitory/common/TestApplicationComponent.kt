package leo.me.la.flitory.common

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import leo.me.la.flitory.FlickrApplication
import leo.me.la.flitory.di.ApplicationModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        TestSearchPhotoModule::class
    ])
internal interface TestApplicationComponent : AndroidInjector<FlickrApplication>
