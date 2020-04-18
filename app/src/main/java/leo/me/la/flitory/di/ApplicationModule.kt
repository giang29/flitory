package leo.me.la.flitory.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

@Module
object ApplicationModule {
    @Provides
    fun provideDispatcher(): CoroutineContext = Dispatchers.Default
}
