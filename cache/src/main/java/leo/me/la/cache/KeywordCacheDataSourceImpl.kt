package leo.me.la.cache

import leo.me.la.common.model.Keyword
import leo.me.la.data.source.KeywordCacheDataSource

internal class KeywordCacheDataSourceImpl: KeywordCacheDataSource {
    override suspend fun getPredefinedKeywords(): List<Keyword> {
        return predefinedKeywords
    }
}
