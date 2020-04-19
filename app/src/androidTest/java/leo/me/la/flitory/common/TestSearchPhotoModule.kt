package leo.me.la.flitory.common

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import io.mockk.mockk
import leo.me.la.flitory.photos.SearchPhotoFragment
import leo.me.la.presentation.SearchPhotoViewModel
import leo.me.la.presentation.ViewModelBuilder
import leo.me.la.presentation.ViewModelKey

@Module
abstract class TestSearchPhotoModule {
    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class,
            SearchPhotoVMModule::class
        ]
    )
    internal abstract fun tasksFragment(): SearchPhotoFragment
}

@Module
object SearchPhotoVMModule {

    @Provides
    @IntoMap
    @ViewModelKey(SearchPhotoViewModel::class)
    fun bindViewModel(): ViewModel {
        return mockedSearchPhotoViewModel
    }
}

internal val mockedSearchPhotoViewModel = mockk<SearchPhotoViewModel>(relaxUnitFun = true)
