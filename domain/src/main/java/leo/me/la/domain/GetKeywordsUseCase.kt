package leo.me.la.domain

import leo.me.la.common.model.Keyword
import leo.me.la.domain.repository.KeywordRepository
import javax.inject.Inject

interface GetKeywordsUseCase {
    suspend fun execute() : List<Keyword>
}

internal class GetKeywordsUseCaseImpl @Inject constructor(
    private val keywordRepository: KeywordRepository
) : GetKeywordsUseCase {

    override suspend fun execute(): List<Keyword> {
        return keywordRepository.getKeywords()
    }
}
