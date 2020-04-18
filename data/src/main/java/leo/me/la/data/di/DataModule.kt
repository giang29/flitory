package leo.me.la.data.di

import dagger.Binds
import dagger.Module
import leo.me.la.data.KeywordRepositoryImpl
import leo.me.la.data.PhotoRepositoryImpl
import leo.me.la.domain.repository.KeywordRepository
import leo.me.la.domain.repository.PhotoRepository

@Module
abstract class DataModule {
    @Binds
    internal abstract fun provideKeywordRepo(keywordRepositoryImpl: KeywordRepositoryImpl):
        KeywordRepository

    @Binds
    internal abstract fun providePhotoRepo(photoRepositoryImpl: PhotoRepositoryImpl):
        PhotoRepository
}
