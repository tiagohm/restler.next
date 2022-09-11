package br.tiagohm.restler.http

data class HttpRequestBody(
    val type: HttpRequestBodyType,
    val text: String? = null,
    val file: String? = null,
    val form: List<FormItem> = emptyList(),
    val multipart: List<MultipartItem> = emptyList(),
) {

    data class FormItem(
        val name: String,
        val value: String,
    )

    enum class MultipartType {
        TEXT,
        FILE,
    }

    data class MultipartItem(
        val type: MultipartType,
        val name: String,
        val value: String,
        val filename: String = "",
        val headers: List<String> = emptyList(),
    ) {

        companion object {

            fun text(name: String, value: String) = MultipartItem(MultipartType.TEXT, name, value)

            fun file(
                name: String,
                filepath: String,
                filename: String = "",
                headers: List<String> = emptyList(),
            ) = MultipartItem(MultipartType.FILE, name, filepath, filename, headers)
        }
    }

    companion object {

        val NONE = HttpRequestBody(HttpRequestBodyType.NONE)

        fun text(text: String) = HttpRequestBody(HttpRequestBodyType.TEXT, text = text)

        fun file(path: String) = HttpRequestBody(HttpRequestBodyType.FILE, file = path)

        fun form(form: List<FormItem>) = HttpRequestBody(HttpRequestBodyType.FORM, form = form)

        fun multipart(multipart: List<MultipartItem>) = HttpRequestBody(HttpRequestBodyType.MULTIPART, multipart = multipart)
    }
}
