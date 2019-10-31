package leo.me.la.data

import leo.me.la.domain.repository.KeywordRepository
import leo.me.la.domain.repository.PhotoRepository
import org.koin.dsl.module

val dataModule = module {
    factory<PhotoRepository> { PhotoRepositoryImpl(get()) }
    factory<KeywordRepository> { KeywordRepositoryImpl(get()) }
}
