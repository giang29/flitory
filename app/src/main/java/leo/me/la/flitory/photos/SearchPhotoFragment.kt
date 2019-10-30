package leo.me.la.flitory.photos

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.f_search_photo.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import leo.me.la.flitory.R
import leo.me.la.flitory.base.BaseFragment
import leo.me.la.flitory.base.PageLoadingHandler
import leo.me.la.flitory.util.DebouncingQueryTextListener
import leo.me.la.presentation.SearchPhotoViewModel
import leo.me.la.presentation.SearchPhotoViewState

@FlowPreview
@ExperimentalCoroutinesApi
internal class SearchPhotoFragment :
    BaseFragment<SearchPhotoViewModel, SearchPhotoViewState>(SearchPhotoViewModel::class) {

    override val layout: Int = R.layout.f_search_photo

    private val pageLoadingHandler = object : PageLoadingHandler() {
        override fun onLoadNextPage() {
            viewModel.loadNextPage()
        }
    }
    private val photoController by lazy {
        PhotoController(
            pageLoadingHandler,
            viewLifecycleOwner.lifecycleScope
        ) { viewModel.loadNextPage() }
    }
    private lateinit var searchView: SearchView
    private var snackBar: Snackbar? = null

    override fun render(viewState: SearchPhotoViewState) {
        snackBar?.dismiss()
        when (viewState) {
            SearchPhotoViewState.Idling -> {
                photosList.isVisible = false
                loadingProgress.isVisible = false
            }
            SearchPhotoViewState.Searching -> {
                photosList.isVisible = false
                loadingProgress.isVisible = true
            }
            is SearchPhotoViewState.PhotosFetched -> {
                photosList.isVisible = true
                loadingProgress.isVisible = false
                photoController.setData(viewState.photos, false, false)
                pageLoadingHandler.hasNextPage = viewState.page < viewState.totalPages
            }
            is SearchPhotoViewState.LoadingNextPage -> {
                photoController.setData(viewState.photos, true, false)
            }
            is SearchPhotoViewState.LoadPageFailed -> {
                photoController.setData(viewState.photos, false, true)
            }
            is SearchPhotoViewState.SearchFailed -> {
                hideSoftKeyboard()
                photosList.isVisible = false
                loadingProgress.isVisible = false
                snackBar = Snackbar.make(photosList, getString(R.string.general_error_message), Snackbar
                    .LENGTH_INDEFINITE)
                    .setAction(R.string.retry) { viewModel.searchPhotos(viewState.keyword) }
                    .also { it.show() }
            }
            is SearchPhotoViewState.NotFound -> {
                hideSoftKeyboard()
                photosList.isVisible = false
                loadingProgress.isVisible = false
                snackBar = Snackbar.make(photosList, getString(R.string.not_found, viewState.keyword), Snackbar
                    .LENGTH_LONG)
                    .also { it.show() }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        toolbar.apply {
            inflateMenu(R.menu.menu_search)
        }.run {
            val searchItem = menu.findItem(R.id.action_search)
            (searchItem.actionView as SearchView)
                .apply {
                    queryHint = getString(R.string.search_photos)
                    setIconifiedByDefault(false)
                    setOnQueryTextListener(
                        DebouncingQueryTextListener(
                            viewLifecycleOwner
                        ) { newText ->
                            newText?.let {
                                if (it.isEmpty()) {
                                    viewModel.resetSearch()
                                } else {
                                    viewModel.searchPhotos(it)
                                }
                            }
                        }
                    )
                    clearFocus()
                }
                .run { searchView = this }
        }
        photosList.layoutManager = GridLayoutManager(requireContext(), 2)
        photosList.setController(photoController)
    }
}
