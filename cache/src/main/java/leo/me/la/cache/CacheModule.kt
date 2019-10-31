package leo.me.la.cache

import leo.me.la.data.source.KeywordCacheDataSource
import org.koin.dsl.module

val cacheModule = module {
    single<KeywordCacheDataSource> {
        KeywordCacheDataSourceImpl()
    }
}
