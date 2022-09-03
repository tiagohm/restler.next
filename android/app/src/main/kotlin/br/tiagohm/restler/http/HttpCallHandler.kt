package br.tiagohm.restler.http

import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel

class HttpCallHandler : MethodChannel.MethodCallHandler {

    private var channel: MethodChannel? = null

    fun startListening(messenger: BinaryMessenger) {
        if (channel != null) stopListening()

        channel = MethodChannel(messenger, "http")
        channel!!.setMethodCallHandler(this)
    }

    fun stopListening() {
        channel?.setMethodCallHandler(null)
        channel = null
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        try {
            val data = when (call.method) {
                "execute" -> "OK"
                else -> return result.notImplemented()
            }

            result.success(data)
        } catch (e: Exception) {
            result.error("ERROR", e.message, "")
        }
    }
}
