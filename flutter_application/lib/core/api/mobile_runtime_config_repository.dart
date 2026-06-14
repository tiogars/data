import 'package:flutter_application/core/api/mobile_runtime_config.dart';
import 'package:shared_preferences/shared_preferences.dart';

class MobileRuntimeConfigRepository {
  const MobileRuntimeConfigRepository();

  static const String _gatewayBaseUrlKey = 'gateway_base_url';
  static const String _jwtTokenKey = 'gateway_jwt_token';

  Future<MobileRuntimeConfig> load() async {
    final prefs = await SharedPreferences.getInstance();

    final gatewayBaseUrl = prefs.getString(_gatewayBaseUrlKey)?.trim();
    final jwtToken = prefs.getString(_jwtTokenKey)?.trim();

    return MobileRuntimeConfig(
      gatewayBaseUrl: (gatewayBaseUrl == null || gatewayBaseUrl.isEmpty)
          ? MobileRuntimeConfig.local.gatewayBaseUrl
          : gatewayBaseUrl,
      jwtToken: (jwtToken == null || jwtToken.isEmpty) ? null : jwtToken,
    );
  }

  Future<void> save(MobileRuntimeConfig config) async {
    final prefs = await SharedPreferences.getInstance();

    await prefs.setString(_gatewayBaseUrlKey, config.gatewayBaseUrl.trim());

    final token = config.jwtToken?.trim();
    if (token == null || token.isEmpty) {
      await prefs.remove(_jwtTokenKey);
      return;
    }

    await prefs.setString(_jwtTokenKey, token);
  }
}
