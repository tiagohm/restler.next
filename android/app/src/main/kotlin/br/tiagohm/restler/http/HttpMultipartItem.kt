package br.tiagohm.restler.http

data class HttpMultipartItem(
    val type: HttpMultipartType,
    val name: String,
    val value: String,
    val filename: String = "",
    val headers: List<String> = emptyList(),
) {

    companion object {

        fun text(name: String, value: String) = HttpMultipartItem(HttpMultipartType.TEXT, name, value)

        fun file(
            name: String,
            filepath: String,
            filename: String = "",
            headers: List<String> = emptyList(),
        ) = HttpMultipartItem(HttpMultipartType.FILE, name, filepath, filename, headers)
    }
}
