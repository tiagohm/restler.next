package br.tiagohm.restler.http

import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import okhttp3.*
import java.io.IOException
import java.util.concurrent.*

class HttpCallHandler : MethodChannel.MethodCallHandler {

    private var channel: MethodChannel? = null
    private val connectionPool = ConnectionPool(32, 5L, TimeUnit.MINUTES)
    private val cachedHttpClient = HashMap<HttpConnection, OkHttpClient>()
    private val cancellableCall = HashMap<String, Pair<Call, MethodChannel.Result>>()

    fun startListening(messenger: BinaryMessenger) {
        if (channel != null) stopListening()

        channel = MethodChannel(messenger, "http")
        channel!!.setMethodCallHandler(this)
    }

    fun stopListening() {
        channel?.setMethodCallHandler(null)
        channel = null

        connectionPool.evictAll()

        cachedHttpClient.clear()
        cancellableCall.clear()
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        try {
            when (call.method) {
                "execute" -> execute(HttpRequest.from(call), OneShotResult(result))
                "cancel" -> cancel(call.string("id")!!, result)
                else -> return result.notImplemented()
            }
        } catch (e: Throwable) {
            result.error("ERROR", e.message, null)
        }
    }

    @Synchronized
    private fun createHttpClient(connection: HttpConnection): OkHttpClient {
        return if (connection in cachedHttpClient) cachedHttpClient[connection]!!
        else OkHttpClient.Builder()
            .followRedirects(connection.followRedirects)
            .followSslRedirects(connection.followSslRedirects)
            .callTimeout(connection.callTimeout, TimeUnit.MILLISECONDS)
            .connectTimeout(connection.connectTimeout, TimeUnit.MILLISECONDS)
            .readTimeout(connection.readTimeout, TimeUnit.MILLISECONDS)
            .writeTimeout(connection.writeTimeout, TimeUnit.MILLISECONDS)
            .connectionPool(connectionPool)
            .addInterceptor(GzipBrotliDeflateInterceptor)
            .build()
            .also { cachedHttpClient[connection] = it }
    }

    private fun execute(
        httpRequest: HttpRequest,
        result: MethodChannel.Result,
    ) {
        val client = createHttpClient(httpRequest.connection)
        val request = httpRequest.toRequest()
        val call = client.newCall(request)

        httpRequest.id?.also { cancellableCall[it] = call to result }

        val callback = HttpCallback(httpRequest, result)
        call.enqueue(callback)
    }

    private fun cancel(
        id: String,
        result: MethodChannel.Result,
    ) {
        val (call, callResult) = cancellableCall[id] ?: return result.success(null)

        synchronized(call) {
            if (!call.isCanceled()) {
                call.cancel()
                callResult.error("ERROR", "cancel", null)
                result.success(true)
            } else {
                result.success(false)
            }
        }
    }

    private inner class HttpCallback(
        private val httpRequest: HttpRequest,
        private val result: MethodChannel.Result,
    ) : Callback {

        override fun onFailure(
            call: Call,
            e: IOException,
        ) {
            e.printStackTrace()
            result.error("ERROR", e.message, null)
            onComplete(call)
        }

        override fun onResponse(
            call: Call,
            response: Response,
        ) {
            result.success(HttpResponse.from(response))
            onComplete(call)
        }

        private fun onComplete(call: Call) {
            synchronized(call) {
                cancellableCall.remove(httpRequest.id)
            }
        }
    }
}
