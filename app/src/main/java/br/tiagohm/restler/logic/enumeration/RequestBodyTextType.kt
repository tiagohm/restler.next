package br.tiagohm.restler.logic.enumeration

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType

enum class RequestBodyTextType(val mediaType: MediaType) {
    PLAIN("text/plain".toMediaType()),
    JS("text/javascript".toMediaType()),
    JSON("application/json".toMediaType()),
    HTML("text/html".toMediaType()),
    XML("application/xml".toMediaType()),
}
