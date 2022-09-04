package br.tiagohm.restler.http

import android.os.Looper
import io.flutter.plugin.common.MethodChannel

class MainLooperResult(result: MethodChannel.Result) : LooperResult(result, Looper.getMainLooper())
