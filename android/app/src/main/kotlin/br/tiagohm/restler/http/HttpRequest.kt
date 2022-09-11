package br.tiagohm.restler.http

import io.flutter.plugin.common.MethodCall
import okhttp3.*
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.internal.EMPTY_REQUEST
import java.io.File

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
        else if (body.type == HttpRequestBodyType.NONE) EMPTY_REQUEST
        else if (body.type == HttpRequestBodyType.TEXT) makeTextBody()
        else if (body.type == HttpRequestBodyType.FILE) makeFileBody()
        else if (body.type == HttpRequestBodyType.FORM) makeFormBody()
        else if (body.type == HttpRequestBodyType.MULTIPART) makeMultipartBody()
        else null
    }

    private fun makeTextBody(): RequestBody {
        return (body!!.text ?: "").toRequestBody()
    }

    private fun makeFileBody(): RequestBody {
        val file = File(body!!.file!!)
        return file.asRequestBody()
    }

    private fun makeFormBody(): RequestBody {
        val builder = FormBody.Builder()
        body!!.form.forEach { builder.add(it.name, it.value) }
        return builder.build()
    }

    private fun makeMultipartBody(): RequestBody {
        val builder = MultipartBody.Builder()

        // TODO: Allow set type.
        builder.setType(MultipartBody.FORM)

        body!!.multipart.forEach {
            if (it.type == HttpRequestBody.MultipartType.TEXT) {
                builder.addFormDataPart(it.name, it.value)
            } else {
                val file = File(it.value)
                // TODO: Support auto and user-defined mime-type?
                val body = file.asRequestBody()
                // TODO: Support headers?
                builder.addFormDataPart(it.name, it.filename.ifBlank { file.name }, body)
            }
        }

        return builder.build()
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
            val body = call.json<HttpRequestBody>("body")
            val connection = call.json<HttpConnection>("connection") ?: HttpConnection.DEFAULT
            return HttpRequest(id, method, uri, headers, body, connection)
        }
    }
}
