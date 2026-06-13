# Setup Android (Flutter)

## Prérequis

- Flutter SDK (compatible Dart `^3.12.2`)
- Android Studio + Android SDK
- un émulateur Android ou un appareil physique

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

## Dépannage

- Si l'émulateur n'est pas visible, exécuter `flutter devices`.
- Si le build échoue sur Android SDK, vérifier `sdkmanager --list` et les licences Android.
- Si l'API locale est inaccessible, vérifier que `data-gateway` tourne sur `http://localhost:8081`.
