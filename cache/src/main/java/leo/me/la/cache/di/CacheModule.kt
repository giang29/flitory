package leo.me.la.cache.di

import dagger.Binds
import dagger.Module
import leo.me.la.cache.KeywordCacheDataSourceImpl
import leo.me.la.data.source.KeywordCacheDataSource
import javax.inject.Singleton

@Module
abstract class CacheModule {

    @Binds
    @Singleton
    internal abstract fun provideKeywordCacheDataSource(keywordCacheDataSourceImpl: KeywordCacheDataSourceImpl):
        KeywordCacheDataSource
}
