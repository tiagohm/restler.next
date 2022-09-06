@file:Suppress("NOTHING_TO_INLINE")

package br.tiagohm.restler.http

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.flutter.plugin.common.MethodCall

internal val JSON = jacksonObjectMapper()

inline fun MethodCall.string(key: String) = argument<String>(key)

inline fun <reified T : Enum<T>> MethodCall.enum(key: String) = enumValueOf<T>(string(key)!!)

inline fun <T> MethodCall.list(key: String) = argument<List<T>>(key)

fun <T> MethodCall.json(key: String, type: Class<T>) = string(key)?.let { JSON.readValue(it, type) }

inline fun <reified T> MethodCall.json(key: String) = json(key, T::class.java)
