package leo.me.la.flitory

import leo.me.la.common.TAG_BOOLEAN_DEBUG
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single(named(TAG_BOOLEAN_DEBUG)) {
        BuildConfig.DEBUG
    }
}
