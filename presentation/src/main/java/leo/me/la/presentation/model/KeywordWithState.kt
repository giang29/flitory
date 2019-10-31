package leo.me.la.presentation.model

import leo.me.la.common.model.Keyword

data class KeywordWithState(
    val keyword: String,
    val subKeywords: List<KeywordWithState>? = null,
    val closed: Boolean
)

internal fun Keyword.toKeywordWithState(): KeywordWithState {
    return KeywordWithState(
        keyword,
        subKeywords?.map { it.toKeywordWithState() },
        true
    )
}
