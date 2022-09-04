package br.tiagohm.restler.http

import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import okhttp3.*
import okhttp3.internal.EMPTY_BYTE_ARRAY
import java.io.IOException
import java.util.concurrent.*

class HttpCallHandler : MethodChannel.MethodCallHandler {

    private var channel: MethodChannel? = null
    private var connectionPool: ConnectionPool? = null
    private val cachedHttpClient = HashMap<HttpConnection, OkHttpClient>()

    fun startListening(messenger: BinaryMessenger) {
        if (channel != null) stopListening()

        channel = MethodChannel(messenger, "http")
        channel!!.setMethodCallHandler(this)
    }

    fun stopListening() {
        channel?.setMethodCallHandler(null)
        channel = null

        connectionPool?.evictAll()
        connectionPool = null

        cachedHttpClient.clear()
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        try {
            when (call.method) {
                "execute" -> execute(HttpRequest.from(call), result)
                else -> return result.notImplemented()
            }
        } catch (e: Throwable) {
            result.error("ERROR", e.message, null)
        }
    }

    @Synchronized
    private fun createConnectionPool() {
        if (connectionPool == null) {
            connectionPool = ConnectionPool(32, 5L, TimeUnit.MINUTES)
        }
    }

    @Synchronized
    private fun createHttpClient(connection: HttpConnection): OkHttpClient {
        return if (connection in cachedHttpClient) cachedHttpClient[connection]!!
        else OkHttpClient.Builder()
            .followRedirects(connection.followRedirects)
            .followSslRedirects(connection.followSslRedirects)
            .callTimeout(connection.callTimeout, TimeUnit.SECONDS)
            .connectTimeout(connection.connectTimeout, TimeUnit.SECONDS)
            .readTimeout(connection.readTimeout, TimeUnit.SECONDS)
            .writeTimeout(connection.writeTimeout, TimeUnit.SECONDS)
            .connectionPool(connectionPool!!)
            .build()
            .also { cachedHttpClient[connection] = it }
    }

    private fun execute(
        httpRequest: HttpRequest,
        result: MethodChannel.Result,
    ) {
        createConnectionPool()
        val client = createHttpClient(HttpConnection.DEFAULT)
        val request = httpRequest.toRequest()
        val call = client.newCall(request)
        val callback = HttpCallback(result)
        call.enqueue(callback)
    }

    private class HttpCallback(private val result: MethodChannel.Result) : Callback {

        override fun onFailure(
            call: Call,
            e: IOException,
        ) {
            result.error("ERROR", e.message, null)
        }

        override fun onResponse(
            call: Call,
            response: Response,
        ) {
            val body = response.body?.bytes() ?: EMPTY_BYTE_ARRAY
            result.success(HttpResponse(response.code, body))
        }
    }
}
