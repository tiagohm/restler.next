package br.tiagohm.restler.http

import io.flutter.embedding.engine.plugins.FlutterPlugin

class HttpPlugin : FlutterPlugin {

    private val handler = HttpCallHandler()

    override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        handler.startListening(binding.binaryMessenger)
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        handler.stopListening()
    }
}
