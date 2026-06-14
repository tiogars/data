# DEVELOPMENT - Flutter Android

## Objectif

Ce guide décrit le workflow de développement du module mobile Flutter.

## Commandes de base

```powershell
flutter pub get
flutter analyze
flutter test
flutter run -d emulator-5554
flutter build apk --debug
```

## Workflow conseillé

1. Implémenter la feature dans `lib/features/<domain>/`
2. Ajouter/mettre à jour les repositories dans `lib/core/`
3. Couvrir la logique par tests unitaires
4. Valider un scénario offline -> online

## Domaines fonctionnels actuels

- GTIN
- Car
- CarMileage
- Android

## Principes d'architecture

- offline-first
- stockage local SQLite
- synchronisation serveur via file locale
- conflit serveur prioritaire

## Raccourcis utiles

```powershell
flutter devices
flutter emulators
flutter emulators --launch <emulator_id>
```

## Authentification OIDC (Keycloak)

### Flux implémenté

Authorization Code + PKCE via `flutter_appauth`. L'app affiche une `LoginPage` au
démarrage si aucune session valide n'est trouvée. Le token est rafraîchi silencieusement
avant chaque sync. Un JWT saisi manuellement dans les paramètres gateway reste disponible
comme **override développeur** (bypass du gate OIDC).

### Paramètres du client Keycloak

| Paramètre | Valeur |
|---|---|
| Realm / Issuer | `https://auth2.tiogars.fr/realms/data` |
| Client ID | `data-mobile-android` |
| Client type | OpenID Connect – **Public** (sans secret) |
| PKCE method | `S256` |
| Valid redirect URIs | `fr.tiogars.data:/oauth2redirect` |
| Valid post-logout redirect URIs | `fr.tiogars.data:/oauth2redirect` |
| Web origins | `fr.tiogars.data:/` |
| Scopes | `openid`, `profile`, `email`, `offline_access` |
| Standard flow | ON |
| Direct access grants | OFF (production) |
| Service accounts | OFF |

> **Mapper audience** : ajouter un mapper de type *Audience* sur le client
> `data-mobile-android` pour inclure dans le claim `aud` l'audience attendue par la
> gateway (ex. `data-gateway`).

### Fichiers concernés dans le code

| Fichier | Rôle |
|---|---|
| `lib/core/auth/auth_config.dart` | Constantes OIDC (issuer, clientId, redirectUri, scopes) |
| `lib/core/auth/auth_token.dart` | Modèle token (access, expiry, refresh, id) |
| `lib/core/auth/auth_repository.dart` | Persistance chiffrée via FlutterSecureStorage / KeyStore Android |
| `lib/core/auth/auth_service.dart` | Login PKCE, refresh silencieux, logout |
| `lib/features/auth/presentation/login_page.dart` | Écran de connexion |
| `android/app/build.gradle.kts` | `applicationId = "fr.tiogars.data"` + `appAuthRedirectScheme` |

### Redirect URI Android

Le schéma `fr.tiogars.data` est déclaré via `manifestPlaceholders` dans
`android/app/build.gradle.kts`. Il correspond à l'`applicationId` et doit être
enregistré **tel quel** dans Keycloak (Valid redirect URIs).

### Variables d'environnement / mode debug

En développement, il est possible de bypasser le login OIDC en renseignant un JWT
directement dans l'écran **Paramétrage gateway** (champ *JWT token – debug*). Ce token
manuel est prioritaire sur le token OIDC.

## Liens

- `README.md`
- `../docs/2-development/instructions/07-mobile-flutter.md`
- `../docs/1-features/1.3-mobile/architecture-sync.md`
