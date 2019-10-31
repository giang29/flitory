package leo.me.la.domain

import leo.me.la.common.model.Keyword
import leo.me.la.domain.repository.KeywordRepository

interface GetKeywordsUseCase {
    suspend fun execute() : List<Keyword>
}

internal class GetKeywordsUseCaseImpl(
    private val keywordRepository: KeywordRepository
) : GetKeywordsUseCase {

    override suspend fun execute(): List<Keyword> {
        return keywordRepository.getKeywords()
    }
}
