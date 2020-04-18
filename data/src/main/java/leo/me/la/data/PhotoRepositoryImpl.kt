package leo.me.la.data

import leo.me.la.common.model.PageOfPhotos
import leo.me.la.data.source.PhotoRemoteDataSource
import leo.me.la.domain.repository.PhotoRepository
import javax.inject.Inject

internal class PhotoRepositoryImpl @Inject constructor(
    private val photoRemoteDataSource: PhotoRemoteDataSource
) : PhotoRepository {
    override suspend fun getImagesByKeyword(keyword: String, page: Int): PageOfPhotos {
        return photoRemoteDataSource.getPhotosByKeyword(keyword, page)
    }
}
