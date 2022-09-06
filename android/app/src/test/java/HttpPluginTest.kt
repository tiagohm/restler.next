import br.tiagohm.restler.http.HttpCallHandler
import br.tiagohm.restler.http.HttpConnection
import br.tiagohm.restler.http.JSON
import com.intuit.karate.core.MockServer
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import java.lang.Thread.sleep
import java.util.*
import kotlin.concurrent.thread

@RunWith(RobolectricTestRunner::class)
class HttpPluginTest {

    private val handler = HttpCallHandler()

    private fun callMethod(name: String, args: Map<String, Any?>, delay: Long = 3000L): MethodChannel.Result {
        val result = mock(MethodChannel.Result::class.java)
        handler.onMethodCall(MethodCall(name, args), result)
        if (delay > 0) sleep(delay)
        return result
    }

    private fun execute(args: Map<String, Any?>, delay: Long = 3000L): MethodChannel.Result {
        return callMethod("execute", args, delay)
    }

    private fun cancel(args: Map<String, Any?>, delay: Long = 0L): MethodChannel.Result {
        return callMethod("cancel", args, delay)
    }

    private fun makeUUID() = UUID.randomUUID().toString()

    @Test
    fun simple() {
        val args = mapOf("uri" to "http://localhost:8080/hello")
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher(200, "Hello Anonymous!")))
    }

    @Test
    fun queryParameter() {
        val args = mapOf("uri" to "http://localhost:8080/hello?name=Tiago")
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher(200, "Hello Tiago!")))
    }

    @Test
    fun callTimeout() {
        val connection = JSON.writeValueAsString(HttpConnection(callTimeout = 1000L))
        val args = mapOf("uri" to "http://localhost:8080/delay?delay=5", "connection" to connection)
        val result = execute(args)
        verify(result, times(1)).error(eq("ERROR"), eq("timeout"), eq(null))
    }

    @Test
    fun status() {
        val statuses = listOf(201, 301, 404, 500, 799)

        for (status in statuses) {
            val args = mapOf("uri" to "http://localhost:8080/status?status=$status")
            val result = execute(args)
            verify(result, times(1)).success(argThat(HttpResponseMatcher(status, "OK")))
        }
    }

    @Test
    fun dontFollowRedirect() {
        val connection = JSON.writeValueAsString(HttpConnection(followRedirects = false))
        val args = mapOf("uri" to "http://localhost:8080/redirect?count=2", "connection" to connection)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher(307, "OK 2")))
    }

    @Test
    fun followRedirect() {
        val connection = JSON.writeValueAsString(HttpConnection(followRedirects = true))
        val args = mapOf("uri" to "http://localhost:8080/redirect?count=2", "connection" to connection)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher(200, "OK 0")))
    }

    @Test
    fun cancel() {
        val id = makeUUID()
        val args = mapOf("id" to id, "uri" to "http://localhost:8080/delay?delay=5")
        thread { sleep(2000L); cancel(args) }
        val result = execute(args, 5000L)
        verify(result, times(1)).error(eq("ERROR"), eq("cancel"), eq(null))
    }

    @Test
    fun headers() {
        val id = makeUUID()
        val headers = listOf("x-auth-token", "AKF50GJ4JG9IE4JIFJI", "content-type", "application/json")
        val args = mapOf("id" to id, "uri" to "http://localhost:8080/headers", "headers" to headers)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher(200, headers = headers)))
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun setUp() {
            MockServer.feature("classpath:server.feature").http(8080).build()
        }
    }
}
