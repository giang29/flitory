package leo.me.la.flitory.photos

import android.widget.LinearLayout
import androidx.lifecycle.MutableLiveData
import androidx.test.rule.ActivityTestRule
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_EXPANDED
import io.mockk.every
import leo.me.la.flitory.HomeActivity
import leo.me.la.flitory.R
import leo.me.la.flitory.checkInvisible
import leo.me.la.flitory.checkVisible
import leo.me.la.flitory.common.mockedSearchPhotoViewModel
import leo.me.la.presentation.SearchPhotoViewState
import org.junit.Rule
import org.junit.Test

class SearchPhotoFragmentTest {
    @get:Rule
    val activityTestRule = object : ActivityTestRule<HomeActivity>(
        HomeActivity::class.java, false,
        true
    ) {
        override fun beforeActivityLaunched() {
            super.beforeActivityLaunched()
            every { mockedSearchPhotoViewModel.viewStates } returns viewState
        }
    }

    private val viewState = MutableLiveData<SearchPhotoViewState>()

    @Test
    fun shouldRenderIdlingState() {
        val behavior = BottomSheetBehavior.from(
            activityTestRule.activity.findViewById<LinearLayout>(R.id.bottomSheet)
        )
        viewState.postValue(SearchPhotoViewState.Idling)

        R.id.loadingProgress.checkInvisible()
        R.id.photosList.checkInvisible()
        R.id.toolbar.checkVisible()
        R.id.fab.checkInvisible()
        assert(behavior.state == STATE_EXPANDED)
    }

    @Test
    fun shouldRenderSearchingState() {
        viewState.postValue(SearchPhotoViewState.Searching)

        R.id.loadingProgress.checkVisible()
        R.id.photosList.checkInvisible()
        R.id.toolbar.checkVisible()
        R.id.fab.checkVisible()
        R.id.bottomSheet.checkInvisible()
    }
}
