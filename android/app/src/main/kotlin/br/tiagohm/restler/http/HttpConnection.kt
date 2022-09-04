package br.tiagohm.restler.http

data class HttpConnection(
    val followRedirects: Boolean = true,
    val followSslRedirects: Boolean = true,
    val callTimeout: Long = 60000L,
    val connectTimeout: Long = 30000L,
    val readTimeout: Long = 30000L,
    val writeTimeout: Long = 30000L,
) {

    companion object {

        val DEFAULT = HttpConnection()
    }
}
