import 'dart:io';

import 'package:flutter/foundation.dart';
import 'package:oidc/oidc.dart';
import 'package:flutter_application/core/auth/auth_config.dart';
import 'package:flutter_application/core/auth/oidc_shared_preferences_store.dart';

/// Gère le cycle de vie OIDC : login (PKCE), refresh silencieux, logout.
class AuthService extends ChangeNotifier {
  AuthService() {
    _manager = OidcUserManager.lazy(
      discoveryDocumentUri: OidcUtils.getOpenIdConfigWellKnownUri(
        Uri.parse(AuthConfig.issuer),
      ),
      clientCredentials: const OidcClientAuthentication.none(
        clientId: AuthConfig.clientId,
      ),
      store: OidcSharedPreferencesStore(),
      settings: OidcUserManagerSettings(
        redirectUri: Uri.parse(_effectiveRedirectUri),
        scope: AuthConfig.scopes,
        options: _buildPlatformOptions(),
      ),
    );
  }

  late final OidcUserManager _manager;

  bool _initialized = false;
  bool _shouldAutoResumeLogin = false;

  bool get initialized => _initialized;
  bool get isAuthenticated => _manager.currentUser != null;
  String? get accessToken => _manager.currentUser?.token.accessToken;

  static String get _effectiveRedirectUri {
    if (!kIsWeb && Platform.isWindows) {
      return AuthConfig.redirectUriWindows;
    }
    return AuthConfig.redirectUriAndroid;
  }

  static OidcPlatformSpecificOptions _buildPlatformOptions() {
    if (!kIsWeb && Platform.isWindows) {
      return const OidcPlatformSpecificOptions(
        windows: OidcPlatformSpecificOptions_Native(),
      );
    }
    return const OidcPlatformSpecificOptions(
      android: OidcPlatformSpecificOptions_AppAuth_Android(),
    );
  }

  bool takeAutoResumeLoginFlag() {
    final value = _shouldAutoResumeLogin;
    _shouldAutoResumeLogin = false;
    return value;
  }

  Future<void> initialize() async {
    await _manager.init();
    _manager.userChanges().listen((_) => notifyListeners());
    _initialized = true;
    notifyListeners();
  }

  Future<void> login() async {
    try {
      await _manager.loginAuthorizationCodeFlow();
      _shouldAutoResumeLogin = false;
      notifyListeners();
    } catch (error, stackTrace) {
      debugPrint('[AuthService.login] Echec OIDC: $error');
      debugPrint('[AuthService.login] Type: ${error.runtimeType}');
      debugPrint('[AuthService.login] StackTrace: $stackTrace');
      rethrow;
    }
  }

  Future<String?> validAccessToken() async {
    final user = _manager.currentUser;
    if (user == null) return null;
    try {
      final refreshed = await _manager.refreshToken();
      return refreshed?.token.accessToken ?? user.token.accessToken;
    } catch (_) {
      await logout();
      return null;
    }
  }

  Future<void> logout() async {
    try {
      await _manager.logout();
    } catch (_) {
      // Deconnexion locale meme si end_session Keycloak echoue.
    }
    notifyListeners();
  }
}
