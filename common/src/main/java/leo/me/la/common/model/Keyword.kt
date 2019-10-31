package leo.me.la.common.model

data class Keyword(val keyword: String, val subKeywords: List<Keyword>? = null)
