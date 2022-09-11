package br.tiagohm.restler.http

import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody.Companion.asResponseBody
import okhttp3.internal.http.promisesBody
import okio.GzipSource
import okio.InflaterSource
import okio.buffer
import okio.source
import org.brotli.dec.BrotliInputStream
import java.util.zip.*

object GzipBrotliDeflateInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain) =
        if (chain.request().header("Accept-Encoding") == null) {
            val request = chain.request().newBuilder()
                .header("Accept-Encoding", "br,gzip,deflate")
                .build()

            val response = chain.proceed(request)

            uncompress(response)
        } else {
            chain.proceed(chain.request())
        }

    internal fun uncompress(response: Response): Response {
        if (!response.promisesBody()) {
            return response
        }

        val body = response.body ?: return response
        val encoding = response.header("Content-Encoding") ?: return response

        val decompressedSource = when {
            encoding.equals("gzip", ignoreCase = true) ->
                GzipSource(body.source()).buffer()
            encoding.equals("br", ignoreCase = true) ->
                BrotliInputStream(body.source().inputStream()).source().buffer()
            encoding.equals("deflate", ignoreCase = true) ->
                InflaterSource(body.source(), Inflater(false)).buffer()
            else -> return response
        }

        return response.newBuilder()
            .removeHeader("Content-Encoding")
            .removeHeader("Content-Length")
            .body(decompressedSource.asResponseBody(body.contentType(), -1))
            .build()
    }
}
