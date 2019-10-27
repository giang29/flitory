package leo.me.la.data.source

import leo.me.la.common.model.PageOfPhotos

interface PhotoRemoteDataSource {
    suspend fun getPhotosByKeyword(text: String, page: Int) : PageOfPhotos
}
