# Flutter Application (Android)

Module mobile du monorepo `data`.

Objectif du module:

- proposer une application Android Flutter
- fonctionner en mode offline-first
- synchroniser avec le backend via gateway
- couvrir les domaines GTIN, Car/CarMileage et Android

## Pré-requis

- Flutter SDK compatible Dart `^3.12.2`
- Android Studio + SDK Android
- émulateur Android ou appareil physique

## Démarrage local

```powershell
Push-Location flutter_application
flutter pub get
flutter run -d emulator-5554
Pop-Location
```

## Qualité et tests

```powershell
Push-Location flutter_application
flutter analyze
flutter test
Pop-Location
```

## Build APK debug

```powershell
Push-Location flutter_application
flutter build apk --debug
Pop-Location
```

## Architecture ciblée

```text
lib/
├── app/
├── core/           # api, database, sync
├── features/       # gtin, vehicles, android_apps
└── shared/
```

## Documentation liée

- `DEVELOPMENT.md`
- `../docs/2-development/instructions/07-mobile-flutter.md`
- `../docs/1-features/1.3-mobile/index.md`
