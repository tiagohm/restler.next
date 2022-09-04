package br.tiagohm.restler.http

data class HttpConnection(
    val followRedirects: Boolean = true,
    val followSslRedirects: Boolean = true,
    val callTimeout: Long = 30,
    val connectTimeout: Long = 30,
    val readTimeout: Long = 30,
    val writeTimeout: Long = 30,
) {

    companion object {

        val DEFAULT = HttpConnection()
    }
}
