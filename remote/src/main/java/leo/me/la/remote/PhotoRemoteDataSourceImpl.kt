package leo.me.la.remote

import com.squareup.moshi.JsonDataException
import leo.me.la.common.model.PageOfPhotos
import leo.me.la.common.model.Photo
import leo.me.la.data.source.PhotoRemoteDataSource

internal class PhotoRemoteDataSourceImpl(
    private val flickrService: FlickrService
): PhotoRemoteDataSource {

    override suspend fun getPhotosByKeyword(text: String, page: Int): PageOfPhotos {
        try {
            return flickrService.fetchImagesAsync(text, page).await().let {
                PageOfPhotos(
                    it.photos.page,
                    it.photos.pages,
                    it.photos.photo.map { p ->
                        val (id, owner, secret, server, farm, title) = p
                        Photo(id, owner, secret, server, farm, title)
                    }
                )
            }
        } catch (j: JsonDataException) {
            // Moshi may wrap JsonDataException inside JsonDataException. Use recursion to get
            // real cause
            fun JsonDataException.getCause(): Throwable {
                return (cause as? JsonDataException)?.getCause() ?: cause ?: this
            }
            throw j.getCause() // Unwrap for meaningful exception
        }
    }
}
