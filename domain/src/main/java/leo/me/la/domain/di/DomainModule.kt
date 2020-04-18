package leo.me.la.domain.di

import dagger.Binds
import dagger.Module
import leo.me.la.domain.GetKeywordsUseCase
import leo.me.la.domain.GetKeywordsUseCaseImpl
import leo.me.la.domain.GetPhotosByKeywordUseCase
import leo.me.la.domain.GetPhotosByKeywordUseCaseImpl

@Module
abstract class DomainModule {
    @Binds
    internal abstract fun provideGetKeywordsUseCase(
        getKeywordsUseCaseImpl: GetKeywordsUseCaseImpl
    ): GetKeywordsUseCase

    @Binds
    internal abstract fun provideGetPhotosByKeywordUseCase(
        getPhotosByKeywordUseCaseImpl: GetPhotosByKeywordUseCaseImpl
    ): GetPhotosByKeywordUseCase
}
