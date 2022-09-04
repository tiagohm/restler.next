package br.tiagohm.restler.http

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.flutter.plugin.common.MethodCall

internal val JSON = jacksonObjectMapper()

@Suppress("NOTHING_TO_INLINE")
inline fun MethodCall.stringOrNull(key: String) = argument<String>(key)

fun MethodCall.string(key: String) = requireNotNull(stringOrNull(key)) { "The parameter '$key' is required" }

fun MethodCall.stringNotEmpty(key: String) = string(key).also { require(it.isNotEmpty()) { "The parameter '$key' must be not empty" } }

fun MethodCall.stringNotBlank(key: String) = string(key).also { require(it.isNotBlank()) { "The parameter '$key' must be not blank" } }

fun <T> MethodCall.json(key: String, type: Class<T>) = stringOrNull(key)?.let { JSON.readValue(it, type) }

inline fun <reified T> MethodCall.json(key: String) = json(key, T::class.java)
