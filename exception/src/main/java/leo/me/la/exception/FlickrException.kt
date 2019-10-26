package leo.me.la.exception

data class FlickrException(val code: Int, override val message: String, val stat: String):
    RuntimeException()
