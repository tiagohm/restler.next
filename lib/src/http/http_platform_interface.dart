import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'http_method_channel.dart';

abstract class HttpPlatform extends PlatformInterface {
  HttpPlatform() : super(token: _token);

  static final Object _token = Object();

  static HttpPlatform _instance = MethodChannelHttp();

  static HttpPlatform get instance => _instance;

  static set instance(HttpPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> execute();
}
