package leo.me.la.domain.repository

import leo.me.la.common.model.PageOfPhotos

interface PhotoRepository {
    suspend fun getImagesByKeyword(keyword: String, page: Int): PageOfPhotos
}
