package leo.me.la.flitory.photos

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_COLLAPSED
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import com.google.android.material.snackbar.BaseTransientBottomBar.ANIMATION_MODE_FADE
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.f_search_photo.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import leo.me.la.flitory.R
import leo.me.la.flitory.base.BaseFragment
import leo.me.la.flitory.base.PageLoadingHandler
import leo.me.la.flitory.util.DebouncingQueryTextListener
import leo.me.la.flitory.util.ThrottlingClickListener
import leo.me.la.presentation.SearchPhotoViewModel
import leo.me.la.presentation.SearchPhotoViewState

@FlowPreview
@ExperimentalCoroutinesApi
internal class SearchPhotoFragment :
    BaseFragment<SearchPhotoViewModel, SearchPhotoViewState>() {

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
    private val keywordController by lazy {
        KeywordController(
            viewLifecycleOwner.lifecycleScope
        ) { searchView.setQuery(it, true) }
    }
    private lateinit var searchView: SearchView
    private var snackBar: Snackbar? = null
    private lateinit var keywordListBehavior: BottomSheetBehavior<LinearLayout>

    override fun render(viewState: SearchPhotoViewState) {
        snackBar?.dismiss()
        when (viewState) {
            SearchPhotoViewState.Idling -> {
                keywordListBehavior.state = STATE_EXPANDED
                photosList.isVisible = false
                loadingProgress.isVisible = false
            }
            is SearchPhotoViewState.KeywordsLoaded -> {
                loadingProgress.isVisible = false
                keywordController.setData(
                    viewState.keywords
                )
                keywordListBehavior.state = STATE_EXPANDED
            }
            SearchPhotoViewState.Searching -> {
                photosList.isVisible = false
                loadingProgress.isVisible = true
                keywordListBehavior.state = STATE_COLLAPSED
            }
            is SearchPhotoViewState.PhotosFetched -> {
                photosList.isVisible = true
                keywordListBehavior.state = STATE_COLLAPSED
                loadingProgress.isVisible = false
                photoController.setData(viewState.photos, false, false)
                pageLoadingHandler.hasNextPage = viewState.page < viewState.totalPages
                if (viewState.page == 1) {
                    photosList.smoothScrollToPosition(0)
                }
            }
            is SearchPhotoViewState.LoadingNextPage -> {
                photoController.setData(viewState.photos, true, false)
            }
            is SearchPhotoViewState.LoadPageFailed -> {
                photoController.setData(viewState.photos, false, true)
            }
            is SearchPhotoViewState.SearchFailed -> {
                keywordListBehavior.state = STATE_COLLAPSED
                hideSoftKeyboard()
                photosList.isVisible = false
                loadingProgress.isVisible = false
                snackBar = Snackbar
                    .make(
                        photosList,
                        getString(R.string.general_error_message),
                        Snackbar.LENGTH_INDEFINITE
                    )
                    .setAction(R.string.retry) { viewModel.searchPhotos(viewState.keyword) }
                    .also { it.show() }
            }
            is SearchPhotoViewState.NotFound -> {
                keywordListBehavior.state = STATE_COLLAPSED
                hideSoftKeyboard()
                photosList.isVisible = false
                loadingProgress.isVisible = false
                snackBar = Snackbar
                    .make(
                        photosList,
                        getString(R.string.not_found, viewState.keyword),
                        Snackbar.LENGTH_LONG
                    )
                    .also {
                        it.animationMode = ANIMATION_MODE_FADE
                        it.show()
                    }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        keywordListBehavior = BottomSheetBehavior.from(bottomSheet)
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

        keywordsList.layoutManager = LinearLayoutManager(requireContext())
        keywordsList.setController(keywordController)

        fab.setOnClickListener(
            ThrottlingClickListener(viewLifecycleOwner, {
                keywordListBehavior.state = STATE_EXPANDED
            })
        )

        keywordListBehavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState != STATE_COLLAPSED)
                    fab.hide()
                else
                    fab.show()
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })
    }

    override val viewModel: SearchPhotoViewModel by viewModels { viewModelFactory }
}
