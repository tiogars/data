import 'package:flutter_secure_storage/flutter_secure_storage.dart';
import 'package:flutter_application/core/auth/auth_token.dart';

/// Persistance sécurisée des tokens OIDC via le KeyStore Android.
class AuthRepository {
  const AuthRepository();

  static const _storage = FlutterSecureStorage(
    aOptions: AndroidOptions(encryptedSharedPreferences: true),
  );

  static const _accessTokenKey = 'auth_access_token';
  static const _accessTokenExpiryKey = 'auth_access_token_expiry';
  static const _refreshTokenKey = 'auth_refresh_token';
  static const _idTokenKey = 'auth_id_token';
  static const _loginInProgressSinceKey = 'auth_login_in_progress_since';
  static const _lastAutoResumeAtKey = 'auth_last_auto_resume_at';

  Future<AuthToken?> load() async {
    final accessToken = await _storage.read(key: _accessTokenKey);
    final expiryRaw = await _storage.read(key: _accessTokenExpiryKey);
    if (accessToken == null || expiryRaw == null) return null;

    final expiry = DateTime.tryParse(expiryRaw);
    if (expiry == null) return null;

    return AuthToken(
      accessToken: accessToken,
      accessTokenExpiry: expiry,
      refreshToken: await _storage.read(key: _refreshTokenKey),
      idToken: await _storage.read(key: _idTokenKey),
    );
  }

  Future<void> save(AuthToken token) async {
    await _storage.write(key: _accessTokenKey, value: token.accessToken);
    await _storage.write(
      key: _accessTokenExpiryKey,
      value: token.accessTokenExpiry.toIso8601String(),
    );
    if (token.refreshToken != null) {
      await _storage.write(key: _refreshTokenKey, value: token.refreshToken);
    }
    if (token.idToken != null) {
      await _storage.write(key: _idTokenKey, value: token.idToken);
    }
    await clearLoginInProgressMarker();
  }

  Future<void> markLoginInProgress() async {
    await _storage.write(
      key: _loginInProgressSinceKey,
      value: DateTime.now().toIso8601String(),
    );
  }

  Future<void> clearLoginInProgressMarker() async {
    await _storage.delete(key: _loginInProgressSinceKey);
  }

  /// Retourne true si un login avait démarré puis a probablement été interrompu.
  Future<bool> consumeInterruptedLoginMarker({Duration? maxAge}) async {
    final value = await _storage.read(key: _loginInProgressSinceKey);
    await clearLoginInProgressMarker();
    if (value == null) return false;

    final startedAt = DateTime.tryParse(value);
    if (startedAt == null) return true;

    final limit = maxAge ?? const Duration(minutes: 15);
    return DateTime.now().difference(startedAt) <= limit;
  }

  /// Enregistre le moment du dernier déclenchement de reprise automatique.
  Future<void> recordAutoResumeAttempt() async {
    await _storage.write(
      key: _lastAutoResumeAtKey,
      value: DateTime.now().toIso8601String(),
    );
  }

  /// Retourne true si un auto-resume a déjà été tenté dans la fenêtre [cooldown].
  Future<bool> isAutoResumeCooldownActive({
    Duration cooldown = const Duration(seconds: 30),
  }) async {
    final value = await _storage.read(key: _lastAutoResumeAtKey);
    if (value == null) return false;
    final lastAt = DateTime.tryParse(value);
    if (lastAt == null) return false;
    return DateTime.now().difference(lastAt) < cooldown;
  }

  Future<void> clear() async {
    await _storage.deleteAll();
  }
}
