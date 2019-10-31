package leo.me.la.flitory.photos

import com.airbnb.epoxy.TypedEpoxyController
import kotlinx.coroutines.CoroutineScope
import leo.me.la.flitory.photos.items.keywordItem
import leo.me.la.presentation.model.KeywordWithState

/**
 * This controller only allows one item to be expanded at a time. If there is an expanded item
 * and user tries to open another one, the previous one will be closed.
 */
internal class KeywordController(
    private val coroutineScope: CoroutineScope,
    private val itemClickListener: (String) -> Unit
) : TypedEpoxyController<List<KeywordWithState>>() {
    override fun buildModels(data: List<KeywordWithState>) {
        buildKeywords(data, 0, data)
    }

    private fun buildKeywords(
        data: List<KeywordWithState>,
        nestedLevel: Int,
        fullList: List<KeywordWithState>
    ) {
        data.forEach {
            keywordItem {
                id(it.keyword)
                keyword(it)
                nestedLevel(nestedLevel)
                coroutineScope(coroutineScope)
                expandCollapseClickListener { keyword ->
                    setData(
                        fullList.updateStateRecursively(keyword, false).first
                    )
                }
                clickListener(itemClickListener)
            }
            if (!it.closed)
                it.subKeywords?.run {
                    buildKeywords(this, nestedLevel + 1, fullList)
                }
        }
    }

    private fun KeywordWithState.closeRecursively(): KeywordWithState {
        return copy(closed = true, subKeywords = subKeywords?.map { it.closeRecursively() })
    }

    private fun List<KeywordWithState>.updateStateRecursively(
        selectedKeyword: KeywordWithState,
        foundSelectedKeyword: Boolean
    ): Pair<List<KeywordWithState>, Boolean> {
        val foundInThisIteration =
            !foundSelectedKeyword && any { it.keyword == selectedKeyword.keyword }
        var found = foundInThisIteration
        val newState = !selectedKeyword.closed
        val updatedList = if (foundInThisIteration) {
            map { keywordWithState ->
                if (keywordWithState.keyword == selectedKeyword.keyword) {
                    if (newState)
                        keywordWithState.closeRecursively()
                    else {
                        keywordWithState.copy(closed = newState)
                    }
                } else {
                    if (!newState)
                        keywordWithState.closeRecursively()
                    else
                        keywordWithState
                }
            }
        } else {
            map { keywordWithState ->
                keywordWithState.copy(
                    subKeywords = if (foundSelectedKeyword || found) {
                        keywordWithState.subKeywords
                    } else {
                        keywordWithState.subKeywords?.updateStateRecursively(
                            selectedKeyword,
                            false
                        )?.also {
                            found = it.second
                        }?.first
                    }
                )
            }
        }
        return Pair(updatedList, foundSelectedKeyword || foundInThisIteration || found)
    }
}
