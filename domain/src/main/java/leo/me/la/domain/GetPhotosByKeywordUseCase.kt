package leo.me.la.domain

import leo.me.la.common.model.PageOfPhotos
import leo.me.la.domain.repository.PhotoRepository
import javax.inject.Inject

interface GetPhotosByKeywordUseCase {
    suspend fun execute(keyword: String, page: Int) : PageOfPhotos
}

internal class GetPhotosByKeywordUseCaseImpl @Inject constructor(
    private val photoRepository: PhotoRepository
) : GetPhotosByKeywordUseCase {

    override suspend fun execute(keyword: String, page: Int): PageOfPhotos {
        return photoRepository.getImagesByKeyword(keyword, page)
    }
}
