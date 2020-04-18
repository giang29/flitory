package leo.me.la.common.di

import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class Debug

@Module
class DebugModule(private val debug: Boolean) {
    @Provides
    @Debug
    @Singleton
    fun provideDebug() = debug
}
