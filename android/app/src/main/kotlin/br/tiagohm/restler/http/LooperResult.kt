package br.tiagohm.restler.http

import android.os.Handler
import android.os.Looper
import io.flutter.plugin.common.MethodChannel

open class LooperResult(
    private val result: MethodChannel.Result,
    looper: Looper,
) : MethodChannel.Result {

    private val handler = Handler(looper)

    override fun success(data: Any?) {
        handler.post { result.success(data) }
    }

    override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {
        handler.post { result.error(errorCode, errorMessage, errorDetails) }
    }

    override fun notImplemented() {
        handler.post(result::notImplemented)
    }
}
