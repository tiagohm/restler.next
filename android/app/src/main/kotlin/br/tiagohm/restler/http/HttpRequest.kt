package br.tiagohm.restler.http

import io.flutter.plugin.common.MethodCall
import okhttp3.Headers
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.internal.EMPTY_REQUEST

data class HttpRequest(
    val id: String?,
    val method: String,
    val uri: String,
    val headers: List<String> = emptyList(),
    val body: HttpRequestBody? = null,
    val connection: HttpConnection = HttpConnection.DEFAULT,
) {

    private fun makeRequestBody(): RequestBody? {
        return if (body == null) null
        else if (body.type == "EMPTY") EMPTY_REQUEST
        else null
    }

    private fun makeHeaders(): Headers {
        return if (headers.isEmpty()) EMPTY_HEADERS
        else Headers.headersOf(*headers.toTypedArray())
    }

    fun toRequest(): Request {
        return Request.Builder()
            .method(method, makeRequestBody())
            .url(uri)
            .headers(makeHeaders())
            .build()
    }

    companion object {

        private val EMPTY_HEADERS = Headers.Builder().build()

        fun from(call: MethodCall): HttpRequest {
            val id = call.string("id")
            val method = call.string("method")?.uppercase() ?: "GET"
            val uri = call.string("uri")!!
            val headers = call.list<String>("headers") ?: emptyList()
            val connection = call.json<HttpConnection>("connection") ?: HttpConnection.DEFAULT
            return HttpRequest(id, method, uri, headers, null, connection)
        }
    }
}
