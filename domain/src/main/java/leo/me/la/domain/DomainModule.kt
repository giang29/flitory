package leo.me.la.domain

import org.koin.dsl.module

val domainModule = module {
    factory<GetPhotosByKeywordUseCase> {
        GetPhotosByKeywordUseCaseImpl(get())
    }
    factory<GetKeywordsUseCase> {
        GetKeywordsUseCaseImpl(get())
    }
}
