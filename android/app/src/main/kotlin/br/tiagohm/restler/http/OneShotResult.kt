package br.tiagohm.restler.http

import io.flutter.plugin.common.MethodChannel
import java.util.concurrent.atomic.*

open class OneShotResult(private val result: MethodChannel.Result) : MethodChannel.Result {

    private val oneShot = AtomicBoolean()

    @Synchronized
    override fun success(data: Any?) {
        if (oneShot.compareAndSet(false, true)) result.success(data)
    }

    @Synchronized
    override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {
        if (oneShot.compareAndSet(false, true)) result.error(errorCode, errorMessage, errorDetails)
    }

    @Synchronized
    override fun notImplemented() {
        if (oneShot.compareAndSet(false, true)) result.notImplemented()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OneShotResult

        if (result != other.result) return false

        return true
    }

    override fun hashCode() = result.hashCode()
}
