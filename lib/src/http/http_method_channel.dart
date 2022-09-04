import 'package:flutter/services.dart';

import 'http_platform_interface.dart';

class MethodChannelHttp extends HttpPlatform {
  final methodChannel = const MethodChannel('http');

  @override
  Future<String?> execute() async {
    final data = {'uri': 'https://google.com'};
    final version = await methodChannel.invokeMethod<String>('execute', data);
    return version;
  }
}
