package leo.me.la.remote

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import leo.me.la.common.model.PageOfPhotos
import leo.me.la.common.model.Photo
import leo.me.la.exception.FlickrException
import leo.me.la.exception.UnexpectedException
import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class PhotoRemoteDataSourceImplTest : BaseApiTest() {
    private val photoRemoteDataSourceImpl =
        PhotoRemoteDataSourceImpl(getMockedRestApi(), TestCoroutineDispatcher())

    @Test
    fun `should map successfully to app model`() {
        runBlocking {
            mockServer.enqueue(
                MockResponse().setBody("json/success.json".readFileContent()).setResponseCode(200)
            )
            val result = photoRemoteDataSourceImpl.getPhotosByKeyword("Animals", 1)

            assertThat(
                result
            ).isEqualTo(
                PageOfPhotos(
                    1,
                    1,
                    listOf(
                        Photo(
                            "48961354943",
                            "183117384@N08",
                            "4128d45088",
                            "65535",
                            66,
                            "Highland Cattle"
                        ),
                        Photo(
                            "48961354338",
                            "45582668@N03",
                            "39815c1990",
                            "65535",
                            66,
                            "Animal sculptures, 01.07.2019."
                        )
                    )
                )
            )
        }
    }

    @Test(expected = FlickrException::class)
    fun `should throw FlickrException`() {
        runBlocking {
            mockServer.enqueue(
                MockResponse().setBody("json/failure.json".readFileContent()).setResponseCode(200)
            )
            photoRemoteDataSourceImpl.getPhotosByKeyword("Animals", 1)
        }
    }

    @Test(expected = UnexpectedException::class)
    fun `should throw UnexpectedException if json is not conformed to api contract`() {
        runBlocking {
            mockServer.enqueue(
                MockResponse().setBody("{}").setResponseCode(200)
            )
            photoRemoteDataSourceImpl.getPhotosByKeyword("Animals", 1)
        }
    }
}
