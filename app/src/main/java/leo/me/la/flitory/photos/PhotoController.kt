package leo.me.la.flitory.photos

import com.airbnb.epoxy.Typed3EpoxyController
import kotlinx.coroutines.CoroutineScope
import leo.me.la.flitory.base.PageLoadingHandler
import leo.me.la.flitory.photos.items.loadingItem
import leo.me.la.flitory.photos.items.photoItem
import leo.me.la.flitory.photos.items.retryItem
import leo.me.la.flitory.util.ThrottlingClickListener
import leo.me.la.presentation.model.PhotoPresentationModel

internal class PhotoController(
    private val pageLoadingHandler: PageLoadingHandler,
    private val lifeCycleScope: CoroutineScope,
    private val retryClickListener: () -> Unit
) : Typed3EpoxyController<List<PhotoPresentationModel>, Boolean, Boolean>() {

    override fun buildModels(
        photos: List<PhotoPresentationModel>,
        isLoading: Boolean,
        failed: Boolean
    ) {
        check(!(isLoading && failed)) { "Cannot load and get failure at the same time" }

        photos.forEach {
            photoItem {
                id(it.id)
                photo(it)
                onBind { _, _, position ->
                    pageLoadingHandler.checkForNewPage(position, photos.size)
                }
            }
        }

        if (isLoading) {
            loadingItem {
                id(0L)
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }
        }

        if (failed) {
            retryItem {
                id(-1L)
                clickListener(ThrottlingClickListener(lifeCycleScope, { retryClickListener() }))
                spanSizeOverride { totalSpanCount, _, _ -> totalSpanCount }
            }
        }
    }
}
