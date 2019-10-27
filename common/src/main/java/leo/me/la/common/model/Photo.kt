package leo.me.la.common.model

data class Photo(
    val id: String,
    val owner: String,
    val secret: String,
    val server: String,
    val farm: Int,
    val title: String
) {
    val thumbnail = "https://farm$farm.staticflickr.com/$server/${id}_${secret}_m.jpg"
}
