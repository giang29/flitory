package leo.me.la.common.model

data class PageOfPhotos(
    val page: Int,
    val totalPages: Int,
    val photos: List<Photo>
)
