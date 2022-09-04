package br.tiagohm.restler.http

import io.flutter.plugin.common.MethodCall
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.internal.EMPTY_REQUEST

data class HttpRequest(
    val method: String,
    val uri: String,
    val body: ByteArray?,
) {

    private fun makeRequestBody(): RequestBody? {
        return if (body == null) null
        else if (body.isEmpty()) EMPTY_REQUEST
        else null
    }

    fun toRequest(): Request {
        return Request.Builder()
            .method(method, makeRequestBody())
            .url(uri)
            .build()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HttpRequest

        if (method != other.method) return false
        if (uri != other.uri) return false
        if (body != null) {
            if (other.body == null) return false
            if (!body.contentEquals(other.body)) return false
        } else if (other.body != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = method.hashCode()
        result = 31 * result + uri.hashCode()
        result = 31 * result + (body?.contentHashCode() ?: 0)
        return result
    }

    companion object {

        fun from(call: MethodCall): HttpRequest {
            val method = call.argument<String>("method") ?: "GET"
            val uri = requireNotNull(call.argument<String>("uri"))
            return HttpRequest(method, uri, null)
        }
    }
}
