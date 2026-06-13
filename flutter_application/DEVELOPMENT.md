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

## Liens

- `README.md`
- `../docs/2-development/instructions/07-mobile-flutter.md`
- `../docs/1-features/1.3-mobile/architecture-sync.md`
