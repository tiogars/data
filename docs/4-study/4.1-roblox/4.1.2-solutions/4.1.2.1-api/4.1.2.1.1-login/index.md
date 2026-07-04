# Roblox API - Authentification par clé API

L'accès à l'API Roblox Open Cloud repose sur une **clé API** associée à un compte Roblox défini. La référence complète est disponible sur [https://create.roblox.com/docs/fr-fr/cloud/auth/api-keys](https://create.roblox.com/docs/fr-fr/cloud/auth/api-keys).

La clé API est créée sur le [Creator Dashboard → Clés API](https://create.roblox.com/dashboard/credentials?activeTab=ApiKeysTab) et stockée en base de données côté application. Elle est ensuite injectée dans chaque requête via l'en-tête `x-api-key`.

---

## Principe

La clé est chargée depuis la base de données au moment de l'appel, puis transmise dans l'en-tête HTTP :

```
x-api-key: <CLE_API>
```

Il n'y a pas de flux de connexion préalable : la clé suffit à identifier et authentifier le compte Roblox associé pour chaque requête.

---

## Exemple d'appel authentifié

```bash
curl --request GET 'https://apis.roblox.com/<endpoint>' \
  --header 'x-api-key: <CLE_API>'
```

Pour les requêtes avec un corps JSON :

```bash
curl --request POST 'https://apis.roblox.com/<endpoint>' \
  --header 'x-api-key: <CLE_API>' \
  --header 'Content-Type: application/json' \
  --data '{ ... }'
```

---

## Flux applicatif

```
Base de données
  └── compte Roblox paramétré (username, universeId, ...)
  └── clé API associée (stockée chiffrée)
          │
          ▼
  Chargement de la clé au moment de l'action
          │
          ▼
  Requête HTTP vers apis.roblox.com
  avec en-tête : x-api-key: <CLE_API>
```

---

## Bonnes pratiques de stockage

- Stocker la clé **chiffrée** en base de données (ne jamais en clair).
- Ne jamais l'exposer dans les logs ni dans les réponses API de l'application.
- Créer une clé par compte paramétré avec les **permissions minimales** nécessaires.
- La clé expire automatiquement après **60 jours sans utilisation** — prévoir une alerte ou un mécanisme de renouvellement.

---

## Dépannage - "token invalid"

Si vous obtenez "token invalid" pendant les tests, vérifier en priorité :

- L'en-tête utilisé est bien **`x-api-key`** (et pas `Authorization: Bearer ...`).
- L'appel cible bien un endpoint **Open Cloud** compatible clé API (`https://apis.roblox.com/...`).
- La clé n'est pas expirée, révoquée, ni liée à un autre compte.
- La clé possède les permissions requises pour l'endpoint visé (scope insuffisant => rejet d'authentification).
- Aucune quote parasite, espace en trop, ni retour à la ligne dans la valeur de la clé.

Commande de vérification minimale :

```bash
curl --request GET 'https://apis.roblox.com/cloud/v2/universes/<UNIVERSE_ID>' \
  --header 'x-api-key: <CLE_API>'
```

Si cet appel fonctionne mais qu'un autre endpoint échoue, le problème vient généralement des permissions de la clé sur cet endpoint spécifique.

