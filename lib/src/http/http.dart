import 'http_platform_interface.dart';

class Http {
  Future<String?> execute() {
    return HttpPlatform.instance.execute();
  }
}
