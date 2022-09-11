import br.tiagohm.restler.http.HttpResponse
import org.mockito.ArgumentMatcher
import java.nio.charset.Charset

class HttpResponseMatcher(
    private val code: Int = 0,
    private val body: ByteArray? = null,
    private val headers: List<String> = emptyList(),
    private val range: IntRange = body?.indices ?: IntRange.EMPTY,
) : ArgumentMatcher<HttpResponse> {

    override fun matches(argument: HttpResponse?): Boolean {
        return argument != null &&
            (code <= 0 || code == argument.code) &&
            (body == null || argument.body.sliceArray(range).contentEquals(body)) &&
            (headers.isEmpty() || argument.headers.containsAll(headers))
    }

    companion object {

        fun string(
            code: Int = 0,
            body: String? = null,
            headers: List<String> = emptyList(),
            charset: Charset = Charsets.UTF_8,
        ) = HttpResponseMatcher(code, body?.toByteArray(charset), headers)
    }
}
