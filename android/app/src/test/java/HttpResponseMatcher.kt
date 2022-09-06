import br.tiagohm.restler.http.HttpResponse
import org.mockito.ArgumentMatcher
import java.nio.charset.Charset

class HttpResponseMatcher(
    val code: Int = 0,
    val body: ByteArray? = null,
    val headers: List<String> = emptyList(),
) : ArgumentMatcher<HttpResponse> {

    constructor(
        code: Int = 0,
        body: String? = null,
        headers: List<String> = emptyList(),
        charset: Charset = Charsets.UTF_8,
    ) : this(code, body?.toByteArray(charset), headers)

    override fun matches(argument: HttpResponse?): Boolean {
        return argument != null &&
            (code <= 0 || code == argument.code) &&
            (body == null || argument.body.contentEquals(body)) &&
            (headers.isEmpty() || argument.headers.containsAll(headers))
    }
}
