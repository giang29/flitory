package leo.me.la.domain.repository

import leo.me.la.common.model.Keyword

interface KeywordRepository {
    suspend fun getKeywords(): List<Keyword>
}
