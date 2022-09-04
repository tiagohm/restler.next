import br.tiagohm.restler.http.HttpCallHandler
import br.tiagohm.restler.http.HttpConnection
import br.tiagohm.restler.http.HttpResponse
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

@RunWith(RobolectricTestRunner::class)
class HttpPluginTest {

    private fun execute(args: Map<String, Any?>, delay: Long = 3000L): MethodChannel.Result {
        val handler = HttpCallHandler()
        val result = mock(MethodChannel.Result::class.java)
        handler.onMethodCall(MethodCall("execute", args), result)
        if (delay > 0) sleep(delay)
        return result
    }

    @Test
    fun simpleRequest() {
        val args = mapOf("uri" to "http://localhost:8080/name")
        val result = execute(args)
        verify(result, times(1)).success(HttpResponse(200, "Anonymous".toByteArray()))
    }

    @Test
    fun requestWithQueryParameter() {
        val args = mapOf("uri" to "http://localhost:8080/name?name=Tiago")
        val result = execute(args)
        verify(result, times(1)).success(HttpResponse(200, "Tiago".toByteArray()))
    }

    @Test
    fun requestWithCallTimeout() {
        val connection = JSON.writeValueAsString(HttpConnection(callTimeout = 1000L))
        val args = mapOf("uri" to "http://localhost:8080/delay?delay=5", "connection" to connection)
        val result = execute(args)
        verify(result, times(1)).error(eq("ERROR"), eq("timeout"), eq(null))
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun setUp() {
            MockServer.feature("classpath:server.feature").http(8080).build()
        }
    }
}
