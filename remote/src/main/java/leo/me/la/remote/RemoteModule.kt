package leo.me.la.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import leo.me.la.common.TAG_BOOLEAN_DEBUG
import leo.me.la.common.TAG_FLICKR_OKHTTP_CLIENT
import leo.me.la.common.TAG_INTERCEPTOR_SEARCH_QUERY_PARAMETER
import leo.me.la.common.TAG_INTERCEPTOR_LOGGING
import leo.me.la.remote.model.FlickrImageAdapter
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.converter.moshi.MoshiConverterFactory

val remoteModule = module {

    single(named(TAG_FLICKR_OKHTTP_CLIENT)) {
        RemoteFactory.buildOkHttpClient(
            listOf(get(named(TAG_INTERCEPTOR_SEARCH_QUERY_PARAMETER))),
            listOf(get(named(TAG_INTERCEPTOR_LOGGING)))
        )
    }

    factory {
        RemoteFactory.buildRestApi(
            "https://www.flickr.com/",
            FlickrService::class.java,
            get(),
            get(named(TAG_FLICKR_OKHTTP_CLIENT))
        )
    }

    single {
        Moshi.Builder()
            .add(FlickrImageAdapter())
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    single {
        MoshiConverterFactory.create(get())
    }

    factory<Interceptor>(named(TAG_INTERCEPTOR_LOGGING)) {
        val isDebug: Boolean = get(named(TAG_BOOLEAN_DEBUG))
        HttpLoggingInterceptor().apply {
            level = if (isDebug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    factory<Interceptor>(named(TAG_INTERCEPTOR_SEARCH_QUERY_PARAMETER)) {
        // For real project, this has to be placed somewhere secure, i.e. CI/CD tool secret
        // But for this project, this is fine
        FlickrQueryParamterInterceptor("b59eaa142fbb03d0ba6c93882fd62e30")
    }
}
