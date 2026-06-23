# 1.3 Mobile Android

Cette section décrit l'application Flutter du monorepo, avec un focus principal sur Android et les flux offline-first.

## Périmètre

- authentification OIDC
- tableau de bord et configuration de la gateway
- gestion des GTIN
- gestion des voitures et du kilométrage par voiture
- gestion des applications Android
- gestion des applications Winget quand la plateforme Flutter le permet
- stockage local SQLite + synchronisation serveur

## Navigation

- [Setup Android](./android-setup.md)
- [Architecture de synchronisation](./architecture-sync.md)
- [Fonctionnalités Android](./features-android.md)

## Ecrans principaux

- **Login**: demarre la connexion OIDC et gere la reprise d'une session interrompue
- **Dashboard**: point d'entree avec indicateurs, synchronisations et parametrage runtime
- **CRUD offline**: chaque domaine ouvre ses listes, details et formulaires hors ligne

## Domaines couverts

| Domaine | Objectif mobile | Plateforme |
|---|---|---|
| GTIN | saisie et consultation hors ligne | Android |
| Car | gestion du parc et selection pour les releves | Android |
| CarMileage | saisie des releves et historique local | Android |
| Android apps | inventaire applicatif Android | Android |
| Winget apps | inventaire applicatif Winget | Flutter Desktop / Windows selon disponibilite |

## Specificites plate-forme

- **Android**: cible principale, avec APK debug et authentification OIDC mobile
- **Windows**: certains ecrans Flutter exposent aussi le domaine Winget pour les usages poste de travail
- **Synchronisation**: la gateway reste l'unique point d'acces reseau, quelle que soit la plateforme cliente
