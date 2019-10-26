package leo.me.la.remote.model

data class ImageRemoteModel(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String
)
