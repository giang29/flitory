package leo.me.la.remote

import leo.me.la.common.TAG_BOOLEAN_DEBUG
import leo.me.la.common.TAG_FLICKR_OKHTTP_CLIENT
import leo.me.la.common.TAG_INTERCEPTOR_SEARCH_QUERY_PARAMETER
import leo.me.la.common.TAG_INTERCEPTOR_LOGGING
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module

val remoteModule = module {

    single(named(TAG_FLICKR_OKHTTP_CLIENT)) {
        RemoteFactory.buildOkHttpClient(
            listOf(get(named(TAG_INTERCEPTOR_SEARCH_QUERY_PARAMETER))),
            listOf(get(named(TAG_INTERCEPTOR_LOGGING)))
        )
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
