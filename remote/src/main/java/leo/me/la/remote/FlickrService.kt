package leo.me.la.remote

import kotlinx.coroutines.Deferred
import leo.me.la.remote.model.ImageResponseRemoteModel
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {

    @GET("services/rest/")
    fun fetchImagesAsync(
        @Query("text") searchTerm: String,
        @Query("page") page: Int,
        @Query("method") method: String = "flickr.photos.search"
    ) : Deferred<ImageResponseRemoteModel>
}
