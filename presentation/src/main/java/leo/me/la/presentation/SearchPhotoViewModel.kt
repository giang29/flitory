package leo.me.la.presentation

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.launch
import leo.me.la.domain.GetPhotosByKeywordUseCase
import leo.me.la.exception.FlickrException
import leo.me.la.presentation.model.PhotoPresentationModel

@ExperimentalCoroutinesApi
@FlowPreview
class SearchPhotoViewModel(
    private val getPhotosByKeywordUseCase: GetPhotosByKeywordUseCase
) : BaseViewModel<SearchPhotoViewState>() {

    private var loadNextPageJob: Job? = null

    private val keywordChangedNotifier: Channel<String> = Channel(Channel.CONFLATED)

    init {
        _viewStates.value = SearchPhotoViewState.Idling
        viewModelScope.launch {
            keywordChangedNotifier.consumeAsFlow()
                .transformLatest {
                    loadNextPageJob?.cancel()
                    _viewStates.value = SearchPhotoViewState.Searching
                    try {
                        val result = getPhotosByKeywordUseCase.execute(it, 1)

                        emit(
                            if (result.totalPages != 0)
                                SearchPhotoViewState.PhotosFetched(
                                    it,
                                    result.photos.map { p -> PhotoPresentationModel.fromPhoto(p) },
                                    1,
                                    result.totalPages
                                )
                            else
                                SearchPhotoViewState.NotFound(it)
                        )
                    } catch (e: Throwable) {
                        when (e) {
                            is FlickrException -> emit(
                                SearchPhotoViewState.SearchFailed(it)
                            )
                            is CancellationException -> {
                            }
                            else -> emit(
                                SearchPhotoViewState.SearchFailed(it)
                            )
                        }
                    }
                }.collect {
                    _viewStates.value = it
                }
        }
    }

    fun resetSearch() {
        loadNextPageJob?.cancel()
        _viewStates.value = SearchPhotoViewState.Idling
    }

    fun searchPhotos(keyword: String) {
        viewModelScope.launch {
            keywordChangedNotifier.send(keyword)
        }
    }

    fun loadNextPage() {
        with(viewStates.value) {
            if (this is SearchPhotoViewState.PhotosFetched || this is SearchPhotoViewState.LoadPageFailed) {
                val totalPages = when (this) {
                    is SearchPhotoViewState.PhotosFetched -> this.totalPages
                    is SearchPhotoViewState.LoadPageFailed -> this.totalPages
                    else -> throw IllegalStateException("The state ${this.javaClass.simpleName} is unexpected")
                }
                val nextPage = when (this) {
                    is SearchPhotoViewState.PhotosFetched -> this.page + 1
                    is SearchPhotoViewState.LoadPageFailed -> this.pageFailedToLoad
                    else -> throw IllegalStateException("The state ${this.javaClass.simpleName} is unexpected")
                }
                val fetchedPhotos = when (this) {
                    is SearchPhotoViewState.PhotosFetched -> this.photos
                    is SearchPhotoViewState.LoadPageFailed -> this.photos
                    else -> throw IllegalStateException("The state ${this.javaClass.simpleName} is unexpected")
                }
                if (totalPages < nextPage) {
                    return@with
                }
                val keyword = when (this) {
                    is SearchPhotoViewState.PhotosFetched -> this.keyword
                    is SearchPhotoViewState.LoadPageFailed -> this.keyword
                    else -> throw IllegalStateException("The state ${this.javaClass.simpleName} is unexpected")
                }
                _viewStates.value = SearchPhotoViewState.LoadingNextPage(fetchedPhotos)
                loadNextPageJob = viewModelScope.launch {
                    try {
                        val nextPagePhotoResult =
                            getPhotosByKeywordUseCase.execute(keyword, nextPage)
                        _viewStates.value = SearchPhotoViewState.PhotosFetched(
                            keyword,
                            (fetchedPhotos + nextPagePhotoResult.photos.map { p ->
                                PhotoPresentationModel.fromPhoto(
                                    p
                                )
                            }).distinct(),
                            nextPage,
                            nextPagePhotoResult.totalPages
                        )
                    } catch (ignored: CancellationException) {

                    } catch (e: Throwable) {
                        _viewStates.value = SearchPhotoViewState.LoadPageFailed(
                            keyword,
                            fetchedPhotos,
                            nextPage,
                            totalPages,
                            e
                        )
                    }
                }
            }
        }
    }

    override fun onCleared() {
        keywordChangedNotifier.cancel()
        loadNextPageJob?.cancel()
        super.onCleared()
    }
}
