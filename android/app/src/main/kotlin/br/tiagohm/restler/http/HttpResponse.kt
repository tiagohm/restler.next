package br.tiagohm.restler.http

import okhttp3.Headers
import okhttp3.Response
import okhttp3.internal.EMPTY_BYTE_ARRAY

data class HttpResponse(
    val code: Int,
    val body: ByteArray,
    val headers: List<String>,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HttpResponse

        if (code != other.code) return false
        if (!body.contentEquals(other.body)) return false
        if (headers != other.headers) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code
        result = 31 * result + body.contentHashCode()
        result = 31 * result + headers.hashCode()
        return result
    }

    override fun toString(): String {
        return "HttpResponse(code=$code, body=${body.decodeToString()}, headers=$headers)"
    }

    companion object {

        private fun Headers.toList(): List<String> {
            val headers = ArrayList<String>(size)

            for (i in 0 until size) {
                headers.add(name(i))
                headers.add(value(i))
            }

            return headers
        }

        fun from(response: Response): HttpResponse {
            return HttpResponse(
                response.code,
                response.body?.bytes() ?: EMPTY_BYTE_ARRAY,
                response.headers.toList(),
            )
        }
    }
}
