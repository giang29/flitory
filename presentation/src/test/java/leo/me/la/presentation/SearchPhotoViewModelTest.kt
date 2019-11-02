package leo.me.la.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.coVerify
import io.mockk.verifyAll
import io.mockk.verifyOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import leo.me.la.common.model.Keyword
import leo.me.la.common.model.PageOfPhotos
import leo.me.la.common.model.Photo
import leo.me.la.domain.GetKeywordsUseCase
import leo.me.la.domain.GetPhotosByKeywordUseCase
import leo.me.la.exception.FlickrException
import leo.me.la.presentation.model.KeywordWithState
import leo.me.la.presentation.model.PhotoPresentationModel
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule

@ExperimentalCoroutinesApi
@FlowPreview
class SearchPhotoViewModelTest {
    private val testDispatcher = TestCoroutineDispatcher()
    private val backgroundContext = TestCoroutineDispatcher()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    private val observer: Observer<SearchPhotoViewState> = mockk {
        every { onChanged(any()) } just Runs
    }

    private val getPhotoUseCase: GetPhotosByKeywordUseCase = mockk()
    private val getKeywordsUseCase: GetKeywordsUseCase = mockk()
    private lateinit var viewModel: SearchPhotoViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        coEvery {
            getKeywordsUseCase.execute()
        } coAnswers {
            emptyList()
        }

        viewModel = SearchPhotoViewModel(getPhotoUseCase, getKeywordsUseCase, backgroundContext)
        viewModel.viewStates.observeForever(observer)
    }

    @After
    fun tearDown() {
        viewModel.viewStates.removeObserver(observer)
        Dispatchers.resetMain() // reset main dispatcher to the original Main dispatcher
    }

    @Test
    fun `should start in Idling state and move to KeywordsLoaded`() {
        coEvery {
            getKeywordsUseCase.execute()
        } coAnswers {
            delay(1000)
            listOf(Keyword("Predefined"))
        }
        val observer = mockk<Observer<SearchPhotoViewState>>(relaxUnitFun = true)
        viewModel = SearchPhotoViewModel(getPhotoUseCase, getKeywordsUseCase, backgroundContext)
        viewModel.viewStates.observeForever(observer)

        coVerify(exactly = 1) {
            observer.onChanged(SearchPhotoViewState.Idling)
        }

        backgroundContext.advanceTimeBy(1000)

        coVerify(exactly = 1) {
            observer.onChanged(
                SearchPhotoViewState.KeywordsLoaded(
                    listOf(
                        KeywordWithState
                            ("Predefined", closed = true)
                    )
                )
            )
        }
    }

    @Test
    fun `should start in Idling state and move to KeywordsLoaded even if an error occur`() {
        coEvery {
            getKeywordsUseCase.execute()
        } coAnswers {
            delay(1000)
            throw RuntimeException("test")
        }

        val observer = mockk<Observer<SearchPhotoViewState>>(relaxUnitFun = true)
        viewModel = SearchPhotoViewModel(getPhotoUseCase, getKeywordsUseCase, backgroundContext)
        viewModel.viewStates.observeForever(observer)

        coVerify(exactly = 1) {
            observer.onChanged(SearchPhotoViewState.Idling)
        }

        backgroundContext.advanceTimeBy(1000)

        coVerify(exactly = 1) {
            observer.onChanged(SearchPhotoViewState.KeywordsLoaded(emptyList()))
        }
    }

    @Test
    fun `should search successfully and move to PhotosFetched state`() {
        val desiredPhotoList = List(3) {
            Photo(
                "48961354943",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Highland Cattle $it"
            )
        }

        coEvery {
            getPhotoUseCase.execute("Batman", 1)
        } returns PageOfPhotos(
            1,
            1,
            desiredPhotoList
        )
        viewModel.searchPhotos("Batman")
        verifyOrder {
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(
                SearchPhotoViewState.PhotosFetched(
                    "Batman",
                    desiredPhotoList.map { PhotoPresentationModel.fromPhoto(it) },
                    1,
                    1
                )
            )
        }
    }

    @Test
    fun `should cancel previous search if new search is dispatched`() {
        val cancelledPhotoList = listOf(
            Photo(
                "48961354944",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Bato mano"
            )
        )
        val desiredPhotoList = listOf(
            Photo(
                "48961354943",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Highland Cattle"
            )
        )
        coEvery {
            getPhotoUseCase.execute("Abc", 1)
        } coAnswers {
            delay(1000)
            PageOfPhotos(1, 1, cancelledPhotoList)
        }
        coEvery {
            getPhotoUseCase.execute("Batman", 1)
        } returns PageOfPhotos(1, 1, desiredPhotoList)
        viewModel.searchPhotos("Abc")
        backgroundContext.advanceTimeBy(500)
        viewModel.searchPhotos("Batman")
        backgroundContext.advanceTimeBy(1000)

        verifyOrder  {
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(
                SearchPhotoViewState.PhotosFetched(
                    "Batman",
                    desiredPhotoList.map { PhotoPresentationModel.fromPhoto(it) },
                    1,
                    1
                )
            )
        }

        verifyAll(inverse = true) {
            observer.onChanged(
                SearchPhotoViewState.PhotosFetched(
                    "Abc",
                    cancelledPhotoList.map { PhotoPresentationModel.fromPhoto(it) },
                    1,
                    1
                )
            )
            observer.onChanged(
                ofType(SearchPhotoViewState.LoadingNextPage::class)
            )
            observer.onChanged(
                SearchPhotoViewState.SearchFailed("Abc")
            )
            observer.onChanged(
                ofType(SearchPhotoViewState.LoadPageFailed::class)
            )
        }
    }

    @Test
    fun `should cancel next page loading if new search is dispatched`() {
        val firstPhotoList = List(10) {
            Photo(
                "48961354943",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Highland Cattle $it"
            )
        }
        val secondPhotoList = listOf(
            Photo(
                "48961354944",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Bato mano"
            )
        )
        with(getPhotoUseCase) {
            coEvery { execute("Abc", 1) } returns PageOfPhotos(1, 200, firstPhotoList)
            coEvery { execute("Abc", 2) } coAnswers {
                delay(100)
                throw Exception()
            }
            coEvery { execute("Batman", 1) } returns PageOfPhotos(1, 1, secondPhotoList)
        }
        viewModel.searchPhotos("Abc")
        backgroundContext.advanceTimeBy(10)
        viewModel.loadNextPage()
        backgroundContext.advanceTimeBy(50)
        viewModel.searchPhotos("Batman")
        backgroundContext.advanceTimeBy(100)
        verifyOrder {
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(
                SearchPhotoViewState.PhotosFetched(
                    "Abc",
                    firstPhotoList.map { PhotoPresentationModel.fromPhoto(it) },
                    1,
                    200
                )
            )
            observer.onChanged(
                SearchPhotoViewState.LoadingNextPage(
                    firstPhotoList.map { PhotoPresentationModel.fromPhoto(it) }
                )
            )
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(
                SearchPhotoViewState.PhotosFetched(
                    "Batman",
                    secondPhotoList.map { PhotoPresentationModel.fromPhoto(it) },
                    1,
                    1
                )
            )
        }
        verifyAll(inverse = true) {
            observer.onChanged(ofType(SearchPhotoViewState.SearchFailed::class))
            observer.onChanged(ofType(SearchPhotoViewState.LoadPageFailed::class))
        }
    }

    @Test
    fun `should reset to page 1 if new search is dispatched`() {
        val firstPhotoList = List(10) {
            Photo(
                "48961354944",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Bato mano $it"
            )
        }
        val secondPhotoList = List(10) {
            Photo(
                "48961354943",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Highland Cattle $it"
            )
        }
        val newSearchPhotoList = listOf(
            Photo(
                "48961354945",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Random"
            )
        )
        with(getPhotoUseCase) {
            coEvery { execute("Abc", 1) } returns PageOfPhotos(1, 200, firstPhotoList)
            coEvery { execute("Abc", 2) } returns PageOfPhotos(2, 200, secondPhotoList)
            coEvery { execute("Batman", 1) } returns PageOfPhotos(1, 1, newSearchPhotoList)
        }
        viewModel.searchPhotos("Abc")
        viewModel.loadNextPage()
        viewModel.searchPhotos("Batman")
        verifyOrder  {
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(
                SearchPhotoViewState.PhotosFetched(
                    "Abc",
                    firstPhotoList.map { PhotoPresentationModel.fromPhoto(it) },
                    1,
                    200
                )
            )
            observer.onChanged(
                SearchPhotoViewState.LoadingNextPage(
                    firstPhotoList.map { PhotoPresentationModel.fromPhoto(it) }
                )
            )
            observer.onChanged(
                SearchPhotoViewState.PhotosFetched(
                    "Abc",
                    (firstPhotoList + secondPhotoList).map { PhotoPresentationModel.fromPhoto(it) },
                    2,
                    200
                )
            )
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(
                SearchPhotoViewState.PhotosFetched(
                    "Batman",
                    newSearchPhotoList.map { PhotoPresentationModel.fromPhoto(it) },
                    1,
                    1
                )
            )
        }
        verifyAll(inverse = true) {
            observer.onChanged(ofType(SearchPhotoViewState.SearchFailed::class))
            observer.onChanged(ofType(SearchPhotoViewState.LoadPageFailed::class))
        }
    }

    @Test
    fun `should move to SearchFailed state`() {
        with(getPhotoUseCase) {
            coEvery { execute("Abc", 1) } throws FlickrException(3, "some error message", "fail")
            coEvery { execute("Def", 1) } throws Exception()
        }
        viewModel.searchPhotos("Abc")
        viewModel.searchPhotos("Def")
        verifyOrder  {
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(SearchPhotoViewState.SearchFailed("Abc"))
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(SearchPhotoViewState.SearchFailed("Def"))
        }
    }

    @Test
    fun `should load next page successfully`() {
        val firstPhotoList = List(10) {
            Photo(
                "48961354943",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Highland Cattle $it"
            )
        }
        val secondPhotoList = List(10) {
            Photo(
                "48961354944",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Bato mano $it"
            )
        }
        with(getPhotoUseCase) {
            coEvery { execute("Abc", 1) } returns PageOfPhotos(1, 200, firstPhotoList)
            coEvery { execute("Abc", 2) } returns PageOfPhotos(2, 200, secondPhotoList)
        }
        viewModel.searchPhotos("Abc")
        viewModel.loadNextPage()
        verifyOrder  {
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(
                SearchPhotoViewState.PhotosFetched(
                    "Abc",
                    firstPhotoList.map { PhotoPresentationModel.fromPhoto(it) },
                    1,
                    200
                )
            )
            observer.onChanged(
                SearchPhotoViewState.LoadingNextPage(
                    firstPhotoList.map { PhotoPresentationModel.fromPhoto(it) }
                )
            )
            observer.onChanged(
                SearchPhotoViewState.PhotosFetched(
                    "Abc",
                    (firstPhotoList + secondPhotoList).map { PhotoPresentationModel.fromPhoto(it) },
                    2,
                    200
                )
            )
        }
    }

    @Test
    fun `shouldn't load next page at all if total page is equal to current page`() {
        val firstPhotoList = List(3) {
            Photo(
                "48961354943",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Highland Cattle $it"
            )
        }
        coEvery {
            getPhotoUseCase.execute("Abc", 1)
        } returns PageOfPhotos(1, 1, firstPhotoList)
        viewModel.searchPhotos("Abc")
        viewModel.loadNextPage()
        verifyOrder  {
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(ofType(SearchPhotoViewState.PhotosFetched::class))
        }
        verifyAll(inverse = true) {
            observer.onChanged(ofType(SearchPhotoViewState.LoadingNextPage::class))
        }
        coVerify { getPhotoUseCase.execute(any(), any()) }
    }

    @Test
    fun `should move to LoadPageFailed state`() {
        val firstPhotoList = List(10) {
            Photo(
                "48961354943",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Highland Cattle $it"
            )
        }
        coEvery {
            getPhotoUseCase.execute("Abc", 1)
        } returns PageOfPhotos(1, 200, firstPhotoList)
        coEvery {
            getPhotoUseCase.execute("Abc", 2)
        } throws Exception()
        viewModel.searchPhotos("Abc")
        viewModel.loadNextPage()
        viewModel.loadNextPage()
        verifyOrder  {
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(ofType(SearchPhotoViewState.PhotosFetched::class))
            observer.onChanged(
                SearchPhotoViewState.LoadingNextPage(
                    firstPhotoList.map { PhotoPresentationModel.fromPhoto(it) }
                )
            )
            observer.onChanged(ofType(SearchPhotoViewState.LoadPageFailed::class))
            observer.onChanged(
                SearchPhotoViewState.LoadingNextPage(
                    firstPhotoList.map { PhotoPresentationModel.fromPhoto(it) }
                )
            )
            observer.onChanged(ofType(SearchPhotoViewState.LoadPageFailed::class))
        }
        coVerify(exactly = 2) {
            getPhotoUseCase.execute("Abc", 2)
        }
    }

    @Test
    fun `should not allow to load next page if the search is reset`() {
        val firstPhotoList = List(10) {
            Photo(
                "48961354943",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Highland Cattle $it"
            )
        }
        val secondPhotoList = List(10) {
            Photo(
                "48961354953",
                "183117384@N08",
                "4128d45088",
                "65535",
                66,
                "Highland Cattler"
            )
        }
        with(getPhotoUseCase) {
            coEvery { execute("Abc", 1) } returns PageOfPhotos(1, 200, firstPhotoList)
            coEvery { execute("Abc", 2) } coAnswers {
                delay(1000)
                PageOfPhotos(2, 200, secondPhotoList)
            }
        }
        viewModel.searchPhotos("Abc")
        backgroundContext.advanceTimeBy(100)
        viewModel.loadNextPage()
        viewModel.resetSearch()
        backgroundContext.advanceTimeBy(1000)
        verifyOrder  {
            observer.onChanged(SearchPhotoViewState.Searching)
            observer.onChanged(ofType(SearchPhotoViewState.PhotosFetched::class))
            observer.onChanged(ofType(SearchPhotoViewState.LoadingNextPage::class))
        }
        verifyAll(inverse = true) {
            observer.onChanged(
                SearchPhotoViewState.PhotosFetched(
                    "Abc",
                    secondPhotoList.map { PhotoPresentationModel.fromPhoto(it) },
                    2,
                    200
                )
            )
        }
    }
}
