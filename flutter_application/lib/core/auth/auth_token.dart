/// Représentation en mémoire des tokens OIDC obtenus depuis Keycloak.
class AuthToken {
  const AuthToken({
    required this.accessToken,
    required this.accessTokenExpiry,
    this.refreshToken,
    this.idToken,
  });

  final String accessToken;
  final DateTime accessTokenExpiry;
  final String? refreshToken;
  final String? idToken;

  /// Vrai si l'access token est expiré (avec une marge de 30 s).
  bool get isExpired =>
      DateTime.now().isAfter(accessTokenExpiry.subtract(const Duration(seconds: 30)));

  bool get hasRefreshToken =>
      refreshToken != null && refreshToken!.isNotEmpty;
}
