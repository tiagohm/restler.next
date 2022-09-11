import br.tiagohm.restler.http.*
import com.intuit.karate.core.MockServer
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.junit.BeforeClass
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*
import org.robolectric.RobolectricTestRunner
import java.lang.Thread.sleep
import java.nio.file.Paths
import java.util.*
import kotlin.concurrent.thread
import kotlin.io.path.readBytes

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
        verify(result, times(1)).success(argThat(HttpResponseMatcher.string(200, "Hello Anonymous!")))
    }

    @Test
    fun queryParameter() {
        val args = mapOf("uri" to "http://localhost:8080/hello?name=Tiago")
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher.string(200, "Hello Tiago!")))
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
            verify(result, times(1)).success(argThat(HttpResponseMatcher.string(status, "OK")))
        }
    }

    @Test
    fun dontFollowRedirect() {
        val connection = JSON.writeValueAsString(HttpConnection(followRedirects = false))
        val args = mapOf("uri" to "http://localhost:8080/redirect?count=2", "connection" to connection)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher.string(307, "OK 2")))
    }

    @Test
    fun followRedirect() {
        val connection = JSON.writeValueAsString(HttpConnection(followRedirects = true))
        val args = mapOf("uri" to "http://localhost:8080/redirect?count=2", "connection" to connection)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher.string(200, "OK 0")))
    }

    @Test
    fun cancel() {
        val args = mapOf("id" to makeUUID(), "uri" to "http://localhost:8080/delay?delay=5")
        thread { sleep(2000L); cancel(args) }
        val result = execute(args, 5000L)
        verify(result, times(1)).error(eq("ERROR"), eq("cancel"), eq(null))
    }

    @Test
    fun headers() {
        val headers = listOf("x-auth-token", "AKF50GJ4JG9IE4JIFJI", "content-type", "application/json")
        val args = mapOf("uri" to "http://localhost:8080/headers", "headers" to headers)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher(200, headers = headers)))
    }

    @Test
    fun jsonTextBody() {
        val text = JSON.writeValueAsString(mapOf("name" to "Tiago"))
        val headers = listOf("content-type", "application/json")
        val body = JSON.writeValueAsString(HttpRequestBody.text(text))
        val args = mapOf("method" to "POST", "uri" to "http://localhost:8080/text", "headers" to headers, "body" to body)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher.string(200, text, headers)))
    }

    @Test
    fun plainTextBody() {
        val text = JSON.writeValueAsString(mapOf("name" to "Tiago"))
        val headers = listOf("content-type", "text/plain")
        val body = JSON.writeValueAsString(HttpRequestBody.text(text))
        val args = mapOf("method" to "POST", "uri" to "http://localhost:8080/text", "headers" to headers, "body" to body)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher.string(200, text, headers)))
    }

    @Test
    fun formBody() {
        val form = listOf(HttpFormItem("username", "tiago"), HttpFormItem("password", "12345678"))
        val headers = listOf("content-type", "application/x-www-form-urlencodekd")
        val body = JSON.writeValueAsString(HttpRequestBody.form(form))
        val args = mapOf("method" to "POST", "uri" to "http://localhost:8080/form", "headers" to headers, "body" to body)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher.string(200, """{"password":["12345678"],"username":["tiago"]}""")))
    }

    @Test
    fun multipartBodyWithoutFile() {
        val multipart = listOf(HttpMultipartItem.text("username", "tiago"), HttpMultipartItem.text("password", "12345678"))
        val body = JSON.writeValueAsString(HttpRequestBody.multipart(multipart))
        val args = mapOf("method" to "POST", "uri" to "http://localhost:8080/multipart", "body" to body)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher.string(200, """{"params":{"password":["12345678"],"username":["tiago"]},"parts":{},"type":"multipart\/form-data;""")))
    }

    @Test
    fun multipartBodyWithFile() {
        val filepath = path("avatar.png").toString()
        val multipart = listOf(HttpMultipartItem.text("username", "tiago"), HttpMultipartItem.text("password", "12345678"), HttpMultipartItem.file("avatar", filepath))
        val body = JSON.writeValueAsString(HttpRequestBody.multipart(multipart))
        val args = mapOf("method" to "POST", "uri" to "http://localhost:8080/multipart", "body" to body)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher.string(200, """{"params":{"password":["12345678"],"username":["tiago"]},"parts":{"avatar":[{"charset":"UTF-8","filename":"avatar.png","transferEncoding":"7bit","name":"avatar","contentType":"application/octet-stream","value":"""")))
    }

    @Test
    fun multipartBodyWithFileAndName() {
        val filepath = path("avatar.png").toString()
        val multipart = listOf(HttpMultipartItem.text("username", "tiago"), HttpMultipartItem.text("password", "12345678"), HttpMultipartItem.file("avatar", filepath, "profile.png"))
        val body = JSON.writeValueAsString(HttpRequestBody.multipart(multipart))
        val args = mapOf("method" to "POST", "uri" to "http://localhost:8080/multipart", "body" to body)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher.string(200, """{"params":{"password":["12345678"],"username":["tiago"]},"parts":{"avatar":[{"charset":"UTF-8","filename":"profile.png","transferEncoding":"7bit","name":"avatar","contentType":"application/octet-stream","value":"""")))
    }

    @Test
    fun fileBody() {
        val filepath = path("avatar.png").toString()
        val body = JSON.writeValueAsString(HttpRequestBody.file(filepath))
        val args = mapOf("method" to "POST", "uri" to "http://localhost:8080/raw", "body" to body)
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher(200, bytes("avatar.png"))))
    }

    @Test
    fun brotli() {
        val args = mapOf("uri" to "http://localhost:8080/brotli")
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher(200, loremIpsum("txt"))))
    }

    @Test
    fun gzip() {
        val args = mapOf("uri" to "http://localhost:8080/gzip")
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher(200, loremIpsum("txt"))))
    }

    @Test
    fun deflate() {
        val args = mapOf("uri" to "http://localhost:8080/deflate")
        val result = execute(args)
        verify(result, times(1)).success(argThat(HttpResponseMatcher(200, loremIpsum("txt"))))
    }

    companion object {

        @JvmStatic
        @BeforeClass
        fun setUp() {
            MockServer.feature("classpath:server.feature").http(8080).build()
        }

        @JvmStatic
        fun path(path: String) = Paths.get(System.getProperty("user.dir"), "/src/test/resources", path)!!

        @JvmStatic
        fun bytes(path: String) = path(path).readBytes()

        @JvmStatic
        fun loremIpsum(extension: String) = bytes("lorem-ipsum.$extension")
    }
}
