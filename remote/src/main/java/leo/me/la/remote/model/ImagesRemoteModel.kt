package leo.me.la.remote.model

data class ImagesRemoteModel(
    val page: Int,
    val pages: Int,
    val perpage: Int,
    val photo: List<ImageRemoteModel>
)
