package leo.me.la.flitory.di

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import leo.me.la.flitory.photos.SearchPhotoFragment
import leo.me.la.presentation.SearchPhotoViewModel
import leo.me.la.presentation.ViewModelBuilder
import leo.me.la.presentation.ViewModelKey

@Module
abstract class SearchPhotoModule {
    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun tasksFragment(): SearchPhotoFragment

    @Binds
    @IntoMap
    @ViewModelKey(SearchPhotoViewModel::class)
    abstract fun bindViewModel(viewmodel: SearchPhotoViewModel): ViewModel
}
