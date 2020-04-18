package leo.me.la.remote.di

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoSet
import kotlinx.coroutines.Dispatchers
import leo.me.la.common.di.Debug
import leo.me.la.data.source.PhotoRemoteDataSource
import leo.me.la.remote.FlickrQueryParamterInterceptor
import leo.me.la.remote.FlickrService
import leo.me.la.remote.PhotoRemoteDataSourceImpl
import leo.me.la.remote.RemoteFactory
import leo.me.la.remote.model.FlickrImageAdapter
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Qualifier
import javax.inject.Singleton
import kotlin.coroutines.CoroutineContext


@Qualifier
internal annotation class NetworkInterceptor

@Qualifier
internal annotation class ApplicationInterceptor

@Qualifier
internal annotation class ApiKey

@Qualifier
internal annotation class IOContext

@Qualifier
internal annotation class BaseUrl

@Module
internal interface RemoteBindsModule {
    @Binds
    @IntoSet
    @ApplicationInterceptor
    fun provideQueryParamInterceptor(flickrQueryParamterInterceptor: FlickrQueryParamterInterceptor):
        Interceptor

    @Binds
    fun providePhotoRemoteDataSource(photoRemoteDataSourceImpl: PhotoRemoteDataSourceImpl):
        PhotoRemoteDataSource
}

@Module(includes = [RemoteBindsModule::class])
class RemoteModule {

    @Provides
    @Singleton
    fun provideJsonAdapterFactory(): JsonAdapter.Factory {
        return KotlinJsonAdapterFactory()
    }

    @Provides
    @Singleton
    internal fun provideMoshi(
        flickrImageAdapter: FlickrImageAdapter,
        jsonAdapterFactory: JsonAdapter.Factory
    ): Moshi {
        return Moshi.Builder()
            .add(flickrImageAdapter)
            .add(jsonAdapterFactory)
            .build()
    }

    @Provides
    @Singleton
    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory {
        return MoshiConverterFactory.create(moshi)
    }

    @Provides
    @Singleton
    @ApiKey
    fun provideApiKey() = "b59eaa142fbb03d0ba6c93882fd62e30"

    @Provides
    @Singleton
    @BaseUrl
    fun provideBaseUrl() = "https://www.flickr.com/"

    @Provides
    @IOContext
    fun provideContext(): CoroutineContext {
        return Dispatchers.IO
    }

    @Provides
    @IntoSet
    @NetworkInterceptor
    fun provideLoggingInterceptor(@Debug debug: Boolean): Interceptor {
        return HttpLoggingInterceptor().apply {
            level =
                if (debug) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideClient(
        @ApplicationInterceptor applicationInterceptor: Set<@JvmSuppressWildcards Interceptor>,
        @NetworkInterceptor networkInterceptor: Set<@JvmSuppressWildcards Interceptor>
    ): OkHttpClient {
        return RemoteFactory.buildOkHttpClient(
            applicationInterceptor.toList(),
            networkInterceptor.toList()
        )
    }

    @Provides
    internal fun provideFlickrService(
        @BaseUrl baseUrl: String, moshiConverterFactory:
        MoshiConverterFactory,
        client: OkHttpClient
    ): FlickrService {
        return RemoteFactory.buildRestApi(
            baseUrl,
            FlickrService::class.java,
            moshiConverterFactory,
            client
        )
    }
}
