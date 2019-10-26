package leo.me.la.exception

data class UnexpectedException(val throwable: Throwable? = null) : RuntimeException()
