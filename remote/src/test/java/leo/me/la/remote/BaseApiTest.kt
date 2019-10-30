package leo.me.la.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import leo.me.la.remote.model.FlickrImageAdapter
import okhttp3.Interceptor
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * Base class to make Api calls
 */
internal abstract class BaseApiTest {

    protected val mockServer: MockWebServer = MockWebServer()

    init {
        mockServer.start()
    }

    protected inline fun <reified T : Any> getMockedRestApi(
        restApiInterceptors: List<Interceptor>? = null,
        restApiNetworkInterceptors: List<Interceptor>? = null
    ): T {
        return RemoteFactory.buildRestApi(
            mockServer.url("/").toString(),
            T::class.java,
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(FlickrImageAdapter())
                    .add(KotlinJsonAdapterFactory())
                    .build()
            ),
            RemoteFactory.buildOkHttpClient(restApiInterceptors, restApiNetworkInterceptors)
        )
    }

    @After
    fun tearDown() {
        mockServer.shutdown()
    }

    /**
     * Adds to the response queue given [List] of [MockResponse]s
     */
    protected fun MockWebServer.enqueue(vararg responses: MockResponse) {
        responses.forEach { this.enqueue(it) }
    }
}
