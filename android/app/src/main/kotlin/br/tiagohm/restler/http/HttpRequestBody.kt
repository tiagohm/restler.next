package br.tiagohm.restler.http

data class HttpRequestBody(
    val type: HttpRequestBodyType,
    val text: String? = null,
    val file: String? = null,
    val form: List<HttpFormItem> = emptyList(),
    val multipart: List<HttpMultipartItem> = emptyList(),
) {

    companion object {

        val NONE = HttpRequestBody(HttpRequestBodyType.NONE)

        fun text(text: String) = HttpRequestBody(HttpRequestBodyType.TEXT, text = text)

        fun file(path: String) = HttpRequestBody(HttpRequestBodyType.FILE, file = path)

        fun form(form: List<HttpFormItem>) = HttpRequestBody(HttpRequestBodyType.FORM, form = form)

        fun multipart(multipart: List<HttpMultipartItem>) = HttpRequestBody(HttpRequestBodyType.MULTIPART, multipart = multipart)
    }
}
