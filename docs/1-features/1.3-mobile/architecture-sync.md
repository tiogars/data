# Architecture Sync (Local + Serveur)

## Principe

L'application mobile fonctionne en mode offline-first:

1. écriture locale immédiate (SQLite)
2. ajout d'une opération dans la file de synchro
3. envoi asynchrone vers le serveur
4. pull périodique des changements distants

Déclencheurs actuellement implémentés dans l'application:

- au démarrage de l'application (bootstrap)
- au retour réseau (changement de connectivité)
- en tâche périodique adaptative (30s, 2min ou 5min selon charge de file)
- push de la file locale vers gateway via `SyncEngine`
- pull serveur (refresh complet des listes) vers SQLite apres traitement de la file
- mode cursor-ready: tentative de pull incremental via curseur (`cursor` / `updatedAfter`) si disponible, sinon fallback automatique en refresh complet
## Domaines synchronisés

- GTIN
- Car
- CarMileage
- Android

## Politique de conflit

Politique courante: serveur prioritaire.

En cas de conflit de version:

- la version serveur est conservée
- la version locale est écrasée
- l'utilisateur reçoit une notification métier si nécessaire

## Endpoints API

Routes consommées via gateway:

- `/gtin`
- `/car`
- `/car-mileage`
- `/android`
- `/api/v1/sync/{domain}/changes`

## État local minimal

- `sync_queue` pour les opérations en attente
- `sync_state` pour les curseurs de reprise
- `deleted_at` dans les tables métier pour soft-delete local

## Soft-delete local

Quand le backend renvoie `deletedIds` en synchro incrémentale:

- l'élément est masqué localement (`deleted_at` non null)
- aucune suppression physique immédiate n'est faite
- l'utilisateur peut déclencher la purge explicite via l'action dédiée dans le dashboard

## Sécurité

- Authentification JWT côté gateway
- token transmis via `Authorization: Bearer <token>`
