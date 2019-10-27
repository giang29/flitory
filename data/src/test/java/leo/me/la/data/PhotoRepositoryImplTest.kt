package leo.me.la.data

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import leo.me.la.common.model.PageOfPhotos
import leo.me.la.common.model.Photo
import leo.me.la.data.source.PhotoRemoteDataSource
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PhotoRepositoryImplTest {
    private val photoRemoteDataSource: PhotoRemoteDataSource = mockk()
    private val photoRepositoryImpl = PhotoRepositoryImpl(photoRemoteDataSource)

    @Test
    fun `should not throw any exception if remote data source gives answer`() {
        val result = PageOfPhotos(
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
        coEvery {
            photoRemoteDataSource.getPhotosByKeyword("batman", 1)
        } coAnswers {
            result
        }

        runBlocking {
            assertThat(photoRepositoryImpl.getImagesByKeyword("batman", 1))
                .isEqualTo(result)
        }
    }

    @Test(expected = RuntimeException::class)
    fun `should propagate exception from remote data source`() {
        coEvery {
            photoRemoteDataSource.getPhotosByKeyword("batman", 1)
        } coAnswers {
            throw RuntimeException("test")
        }

        runBlocking {
            photoRepositoryImpl.getImagesByKeyword("batman", 1)
        }
    }
}
