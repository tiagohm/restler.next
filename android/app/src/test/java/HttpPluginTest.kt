import br.tiagohm.restler.http.HttpCallHandler
import br.tiagohm.restler.http.HttpResponse
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

    @Test
    fun simpleRequest() {
        val handler = HttpCallHandler()
        val args = mapOf("uri" to "http://127.0.0.1:8080/hello?name=Tiago")
        val result = mock(MethodChannel.Result::class.java)
        handler.onMethodCall(MethodCall("execute", args), result)
        sleep(5000)
        verify(result, times(1)).success(HttpResponse(200, "Tiago".toByteArray()))
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun setUp() {
            MockServer.feature("classpath:server.feature").http(8080).build()
        }
    }
}
