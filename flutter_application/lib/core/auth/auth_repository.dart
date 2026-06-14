import 'package:shared_preferences/shared_preferences.dart';
import 'package:flutter_application/core/auth/auth_token.dart';

/// Persistance des tokens OIDC via SharedPreferences.
class AuthRepository {
  const AuthRepository();

  static const _accessTokenKey = 'auth_access_token';
  static const _accessTokenExpiryKey = 'auth_access_token_expiry';
  static const _refreshTokenKey = 'auth_refresh_token';
  static const _idTokenKey = 'auth_id_token';
  static const _loginInProgressSinceKey = 'auth_login_in_progress_since';
  static const _lastAutoResumeAtKey = 'auth_last_auto_resume_at';

  Future<SharedPreferences> _prefs() => SharedPreferences.getInstance();

  Future<AuthToken?> load() async {
    final prefs = await _prefs();
    final accessToken = prefs.getString(_accessTokenKey);
    final expiryRaw = prefs.getString(_accessTokenExpiryKey);
    if (accessToken == null || expiryRaw == null) return null;

    final expiry = DateTime.tryParse(expiryRaw);
    if (expiry == null) return null;

    return AuthToken(
      accessToken: accessToken,
      accessTokenExpiry: expiry,
      refreshToken: prefs.getString(_refreshTokenKey),
      idToken: prefs.getString(_idTokenKey),
    );
  }

  Future<void> save(AuthToken token) async {
    final prefs = await _prefs();
    await prefs.setString(_accessTokenKey, token.accessToken);
    await prefs.setString(
      _accessTokenExpiryKey,
      token.accessTokenExpiry.toIso8601String(),
    );
    if (token.refreshToken != null) {
      await prefs.setString(_refreshTokenKey, token.refreshToken!);
    }
    if (token.idToken != null) {
      await prefs.setString(_idTokenKey, token.idToken!);
    }
    await clearLoginInProgressMarker();
  }

  Future<void> markLoginInProgress() async {
    final prefs = await _prefs();
    await prefs.setString(
      _loginInProgressSinceKey,
      DateTime.now().toIso8601String(),
    );
  }

  Future<void> clearLoginInProgressMarker() async {
    final prefs = await _prefs();
    await prefs.remove(_loginInProgressSinceKey);
  }

  /// Retourne true si un login avait démarré puis a probablement été interrompu.
  Future<bool> consumeInterruptedLoginMarker({Duration? maxAge}) async {
    final prefs = await _prefs();
    final value = prefs.getString(_loginInProgressSinceKey);
    await prefs.remove(_loginInProgressSinceKey);
    if (value == null) return false;

    final startedAt = DateTime.tryParse(value);
    if (startedAt == null) return true;

    final limit = maxAge ?? const Duration(minutes: 15);
    return DateTime.now().difference(startedAt) <= limit;
  }

  /// Enregistre le moment du dernier déclenchement de reprise automatique.
  Future<void> recordAutoResumeAttempt() async {
    final prefs = await _prefs();
    await prefs.setString(
      _lastAutoResumeAtKey,
      DateTime.now().toIso8601String(),
    );
  }

  /// Retourne true si un auto-resume a déjà été tenté dans la fenêtre [cooldown].
  Future<bool> isAutoResumeCooldownActive({
    Duration cooldown = const Duration(seconds: 30),
  }) async {
    final prefs = await _prefs();
    final value = prefs.getString(_lastAutoResumeAtKey);
    if (value == null) return false;
    final lastAt = DateTime.tryParse(value);
    if (lastAt == null) return false;
    return DateTime.now().difference(lastAt) < cooldown;
  }

  Future<void> clear() async {
    final prefs = await _prefs();
    await prefs.remove(_accessTokenKey);
    await prefs.remove(_accessTokenExpiryKey);
    await prefs.remove(_refreshTokenKey);
    await prefs.remove(_idTokenKey);
    await prefs.remove(_loginInProgressSinceKey);
    await prefs.remove(_lastAutoResumeAtKey);
  }
}
