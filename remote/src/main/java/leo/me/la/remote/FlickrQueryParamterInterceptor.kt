package leo.me.la.remote

import okhttp3.Interceptor
import okhttp3.Response

internal class FlickrQueryParamterInterceptor(private val apiKey: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            with(chain.request()) {
                newBuilder().url(
                    url().newBuilder()
                        .addQueryParameter("api_key", apiKey)
                        .addQueryParameter("format", "json")
                        .addQueryParameter("nojsoncallback", "1")
                        .build()
                ).build()
            }
        )
    }
}
