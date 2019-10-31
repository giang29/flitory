package leo.me.la.data

import leo.me.la.common.model.Keyword
import leo.me.la.data.source.KeywordCacheDataSource
import leo.me.la.domain.repository.KeywordRepository

internal class KeywordRepositoryImpl(private val cacheDataSource: KeywordCacheDataSource) :
    KeywordRepository {

    override suspend fun getKeywords(): List<Keyword> {
        return cacheDataSource.getPredefinedKeywords()
    }
}
