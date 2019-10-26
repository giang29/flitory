package leo.me.la.remote.model

import com.squareup.moshi.JsonEncodingException
import com.squareup.moshi.JsonReader
import leo.me.la.exception.FlickrException
import leo.me.la.exception.UnexpectedException
import leo.me.la.remote.readFileContent
import okio.Buffer
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class FlickrImageAdapterTest {

    private val photoSearchAdapter = FlickrImageAdapter()

    @Test
    fun `should parse successfully`() {
        val json = "json/success.json".readFileContent()
        val parsingResult = parseJsonToPhotoResult(json)
        assertThat(parsingResult.photos.page == 1)
        assertThat(parsingResult.photos.pages == 1)
        assertThat(parsingResult.photos.perpage == 100)
        assertThat(parsingResult.photos.photo).isEqualTo(
            listOf(
                ImageRemoteModel(
                    "48961354943",
                    "183117384@N08",
                    "4128d45088",
                    "65535",
                    66,
                    "Highland Cattle"
                ),
                ImageRemoteModel(
                    "48961354338",
                    "45582668@N03",
                    "39815c1990",
                    "65535",
                    66,
                    "Animal sculptures, 01.07.2019."
                )
            )
        )
    }

    @Test(expected = FlickrException::class)
    fun `should throw a FlickrException if stat is "fail"`() {
        val json = "json/failure.json".readFileContent()
        parseJsonToPhotoResult(json)
    }

    @Test(expected = UnexpectedException::class)
    fun `should throw an UnexpectedException if json model is not handled`() {
        val json = "{}"
        parseJsonToPhotoResult(json)
    }

    @Test(expected = JsonEncodingException::class)
    fun `should throw an JsonEncodingException if json is not parse-able`() {
        val json = "{]" //invalid json
        parseJsonToPhotoResult(json)
    }

    private fun parseJsonToPhotoResult(json: String): ImageResponseRemoteModel {
        return photoSearchAdapter.fromJson(
            JsonReader.of(Buffer().writeUtf8(json))
        )
    }
}
