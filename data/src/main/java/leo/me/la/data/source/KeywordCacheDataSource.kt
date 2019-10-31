package leo.me.la.data.source

import leo.me.la.common.model.Keyword

interface KeywordCacheDataSource {
    suspend fun getPredefinedKeywords(): List<Keyword>
}
