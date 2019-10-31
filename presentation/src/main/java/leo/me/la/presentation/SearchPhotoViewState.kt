package leo.me.la.presentation

import leo.me.la.presentation.model.KeywordWithState
import leo.me.la.presentation.model.PhotoPresentationModel

sealed class SearchPhotoViewState : BaseViewState {
    object Idling : SearchPhotoViewState()
    data class KeywordsLoaded(val keywords: List<KeywordWithState>): SearchPhotoViewState()
    object Searching : SearchPhotoViewState()
    data class LoadingNextPage(
        val photos: List<PhotoPresentationModel>
    ) : SearchPhotoViewState()
    data class PhotosFetched(
        val keyword: String,
        val photos: List<PhotoPresentationModel>,
        val page: Int,
        val totalPages: Int
    ) : SearchPhotoViewState()
    data class LoadPageFailed(
        val keyword: String,
        val photos: List<PhotoPresentationModel>,
        val pageFailedToLoad: Int,
        val totalPages: Int,
        val reason: Throwable? = null
    ) : SearchPhotoViewState()
    data class SearchFailed(val keyword: String) : SearchPhotoViewState()
    data class NotFound(val keyword: String): SearchPhotoViewState()
}
