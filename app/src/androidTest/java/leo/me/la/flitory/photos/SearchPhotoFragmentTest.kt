package leo.me.la.flitory.photos
//TODO
//import androidx.lifecycle.MutableLiveData
//import androidx.test.rule.ActivityTestRule
//import io.mockk.every
//import io.mockk.mockk
//import leo.me.la.flitory.HomeActivity
//import leo.me.la.flitory.R
//import leo.me.la.flitory.checkInvisible
//import leo.me.la.flitory.checkVisible
//import leo.me.la.presentation.SearchPhotoViewModel
//import leo.me.la.presentation.SearchPhotoViewState
//import leo.me.la.presentation.baseViewModel
//import org.junit.Rule
//import org.junit.Test
//import org.koin.core.context.loadKoinModules
//import org.koin.dsl.module
//import org.koin.test.KoinTest
//
//class SearchPhotoFragmentTest : KoinTest {
//    @get:Rule
//    val activityTestRule = object : ActivityTestRule<HomeActivity>(
//        HomeActivity::class.java, false,
//        true
//    ) {
//        override fun beforeActivityLaunched() {
//            super.beforeActivityLaunched()
//            every { viewModel.viewStates } returns viewState
//            loadKoinModules(module(override = true) {
//                baseViewModel(override = true) { viewModel }
//            })
//        }
//    }
//
//    private val viewModel = mockk<SearchPhotoViewModel>(relaxUnitFun = true)
//
//    private val viewState = MutableLiveData<SearchPhotoViewState>()
//
//    @Test
//    fun shouldRenderIdlingState() {
//        viewState.postValue(SearchPhotoViewState.Idling)
//
//        R.id.loadingProgress.checkInvisible()
//        R.id.photosList.checkInvisible()
//        R.id.toolbar.checkVisible()
//        R.id.fab.checkInvisible()
//        R.id.bottomSheet.checkVisible()
//    }
//
//    @Test
//    fun shouldRenderSearchingState() {
//        viewState.postValue(SearchPhotoViewState.Searching)
//
//        R.id.loadingProgress.checkVisible()
//        R.id.photosList.checkInvisible()
//        R.id.toolbar.checkVisible()
//        R.id.fab.checkVisible()
//        R.id.bottomSheet.checkInvisible()
//    }
//}
