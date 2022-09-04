package br.tiagohm.restler.http

data class HttpResponse(
    val code: Int,
    val body: ByteArray,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HttpResponse

        if (code != other.code) return false
        if (!body.contentEquals(other.body)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code
        result = 31 * result + body.contentHashCode()
        return result
    }
}
