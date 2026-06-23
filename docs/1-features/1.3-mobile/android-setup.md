# Setup Android (Flutter)

## Prérequis

- Flutter SDK (compatible Dart `^3.12.2`)
- Android Studio + Android SDK
- un émulateur Android ou un appareil physique
- accès à une gateway locale ou distante compatible avec `data-gateway`

## Démarrage rapide

```powershell
Push-Location flutter_application
flutter pub get
flutter run -d emulator-5554
Pop-Location
```

## Build APK debug

```powershell
Push-Location flutter_application
flutter build apk --debug
Pop-Location
```

## Vérifications recommandées

```powershell
Push-Location flutter_application
flutter analyze
flutter test
Pop-Location
```

## Authentification mobile

L'application Android utilise OIDC avec Keycloak:

- client public: `data-mobile-android`
- redirect URI Android: `fr.tiogars.data:/oauth2redirect`
- scopes: `openid profile email offline_access`

En cas d'erreur de retour de session:

- vérifier l'intent filter Android
- vérifier la redirect URI côté Keycloak
- relancer la connexion depuis l'écran `Login`

## Configuration runtime

Le paramétrage réseau se fait dans l'application:

- écran `Paramétrage gateway`
- URL locale par défaut orientée développement
- possibilité d'utiliser un JWT manuel pour les diagnostics

Avant un test sur appareil:

- vérifier que `data-gateway` répond sur l'URL configurée
- vérifier que le réseau de l'appareil permet d'atteindre la machine de développement
- adapter l'URL si `localhost` n'est pas accessible depuis l'émulateur ou le téléphone

## Spécificités Android

- l'application cible d'abord Android pour les domaines GTIN, Car, CarMileage et Android apps
- la synchronisation démarre au lancement puis au retour réseau
- l'APK debug est produit par Flutter dans `build/app/outputs/flutter-apk/`

## Dépannage

- Si l'émulateur n'est pas visible, exécuter `flutter devices`.
- Si le build échoue sur Android SDK, vérifier `sdkmanager --list` et les licences Android.
- Si l'API locale est inaccessible, vérifier que `data-gateway` tourne sur `http://localhost:8081`.
- Si la connexion OIDC expire, vérifier la configuration Keycloak, le schéma `fr.tiogars.data`, et la réception du callback mobile.
