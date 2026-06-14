/// Configuration OIDC partagée pour le client Keycloak Android.
///
/// [issuer]       : URL du realm Keycloak cible.
/// [clientId]     : Client ID Keycloak – doit être de type Public (sans secret).
/// [redirectUri]  : Doit correspondre au manifestPlaceholders["appAuthRedirectScheme"]
///                  dans android/app/build.gradle.kts + être enregistré dans Keycloak.
class AuthConfig {
  AuthConfig._();

  static const String issuer = 'https://auth2.tiogars.fr/realms/data';

  static const String clientId = 'data-mobile-android';

  /// Android : schéma custom (applicationId depuis build.gradle.kts).
  static const String redirectUriAndroid = 'fr.tiogars.data:/oauth2redirect';

  /// Windows / Desktop : loopback HTTP géré par oidc_loopback_listener.
  /// Utilise 127.0.0.1 pour éviter les interceptions de localhost par un proxy local.
  static const String redirectUriWindows = 'http://127.0.0.1';

  static const List<String> scopes = [
    'openid',
    'profile',
    'email',
    // offline_access permet d'obtenir un refresh_token longue durée
    'offline_access',
  ];
}
