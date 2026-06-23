# 07 - Mobile Flutter (Android)

## Objectif

Ce guide définit les conventions pour intégrer et maintenir le module mobile `flutter_application`.

Portée actuelle:

- Android comme cible produit principale
- stockage local SQLite
- synchronisation serveur via gateway
- domaines couverts: GTIN, Car, CarMileage, Android
- parcours additionnels: authentification OIDC, dashboard, paramétrage runtime
- extension desktop existante pour le domaine Winget

## Architecture recommandée

Organisation Flutter:

```text
flutter_application/lib/
├── app/                     # bootstrap, theme, navigation
├── core/
│   ├── api/                 # client HTTP, auth JWT, erreurs
│   ├── database/            # SQLite (drift/sqflite) + migrations
│   └── sync/                # queue locale, pull/push, conflits
├── features/
│   ├── auth/                # ecran de connexion OIDC
│   ├── dashboard/           # accueil, synchronisations, parametrage gateway
│   ├── gtin/
│   ├── vehicles/            # car + car mileage
│   ├── android_apps/
│   └── winget_apps/         # expose surtout sur Windows
└── shared/                  # widgets, utils, modèles partagés
```

Flux de dépendances:

- Presentation -> Domain -> Data
- Data -> Gateway API (`http://localhost:8081` en local)
- Data -> SQLite (cache local et file de synchro)

## Persistance locale

Contraintes:

- toute écriture est persistée localement avant envoi réseau
- une `sync_queue` stocke les opérations en attente
- une table `sync_state` stocke les curseurs de synchro par domaine

Tables minimales:

- `gtin`
- `car`
- `car_mileage`
- `android_app`
- `sync_queue`
- `sync_state`

## Synchronisation

Politique validée:

- conflit: serveur prioritaire
- en cas de divergence, la copie locale est remplacée par la version serveur

Mécanisme attendu:

1. Pull des changements serveur depuis dernier curseur
2. Push des opérations locales en attente (ordre FIFO)
3. Réconciliation locale + mise à jour curseurs
4. Retry avec backoff en cas d'échec réseau

Mécanisme actuellement implémenté:

- push de la file locale vers gateway via `SyncEngine`
- pull cursor-ready: tentative incremental (curseur) puis fallback refresh complet si le backend ne retourne pas de curseur
- déclenchement automatique au démarrage de l'application
- déclenchement automatique quand la connectivité revient

## Intégration API

L'application mobile consomme les routes gateway existantes:

- `/gtin`
- `/car`
- `/car-mileage`
- `/android`
- `/winget`
- `/api/v1/sync/{domain}/changes`

Conventions:

- authentification JWT alignée gateway
- pas de logique métier dans la couche UI
- mapping explicite entre DTO réseau et modèles locaux

## Notes de plateforme

- Android reste la cible produit principale
- certains écrans Flutter vérifient `Platform.isWindows` pour afficher le domaine Winget
- la configuration runtime (URL gateway, JWT manuel de debug) est persistée localement

## Tests

Minimum attendu:

- tests unitaires: repositories et use cases
- tests widget: écrans critiques et formulaires
- tests intégration: scénario offline -> online -> sync

## Commandes utiles

```powershell
Push-Location flutter_application
flutter pub get
flutter analyze
flutter test
flutter run -d emulator-5554
flutter build apk --debug
Pop-Location
```

## Documentation liée

- `flutter_application/README.md`
- `flutter_application/DEVELOPMENT.md`
- `docs/1-features/1.3-mobile/index.md`
- `docs/1-features/1.3-mobile/architecture-sync.md`
- `docs/1-features/1.3-mobile/features-android.md`
