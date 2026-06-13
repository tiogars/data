# Fonctionnalités Android

## GTIN

Capacités visées:

- création et édition en local
- recherche locale
- synchronisation serveur

## Kilométrage des voitures

Capacités visées:

- sélection d'une voiture (`Car`)
- création de relevés (`CarMileage`)
- consultation de l'historique par voiture
- synchronisation locale/serveur

## Applications Android

Capacités visées:

- gestion de la liste (nom, package, catégorie, description)
- édition hors ligne
- synchronisation serveur

## Scénario type offline -> online

1. l'utilisateur saisit des données hors connexion
2. les données sont stockées en SQLite
3. au retour réseau, la file de synchro est rejouée
4. l'état local est réaligné avec la réponse serveur

## Actions disponibles dans le dashboard

- appui sur une carte domaine pour saisir une nouvelle donnée hors ligne
- action "Synchroniser maintenant" dans la barre supérieure
- flux recommande pour le kilometrage: creer/synchroniser d'abord une voiture puis utiliser son identifiant backend (UUID)
