package leo.me.la.cache

import leo.me.la.common.model.Keyword
import leo.me.la.data.source.KeywordCacheDataSource
import javax.inject.Inject

internal class KeywordCacheDataSourceImpl @Inject constructor(): KeywordCacheDataSource {
    override suspend fun getPredefinedKeywords(): List<Keyword> {
        return predefinedKeywords
    }
}
