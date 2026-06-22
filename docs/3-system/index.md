# Système

## Réseau

Le système est organisé autour de trois points d'accès principaux:

| Composant | URL locale | Rôle |
|---|---|---|
| data-web | `http://localhost:5173` | interface Web React |
| data-gateway | `http://localhost:8081` | point d'entrée HTTP, auth, rate-limiting, routage |
| data-server | `http://localhost:8080` | API métier Spring Boot |

## Surface API

### Gateway comme point d'entrée unique

La gateway publie les routes métiers une à une, puis les relaie vers `data-server`.

Domaines routés:

- `/brand`
- `/model`
- `/car`
- `/car-mileage`
- `/brick`
- `/section`
- `/footer-link`
- `/menu-item`
- `/gtin`
- `/android`
- `/winget`
- `/url-manager`
- `/github-repository`
- `/continent`
- `/github-rest-config`
- `/user-account`

Chaque domaine expose en pratique:

- `/domain`
- `/domain/**`

### Route API générique de synchronisation

En complément des routes métiers, la gateway publie aussi:

- `/api/**`

Cette route est utilisée par la synchronisation incrémentale Flutter, notamment via:

- `GET /api/v1/sync/gtin/changes`
- `GET /api/v1/sync/car/changes`
- `GET /api/v1/sync/car-mileage/changes`
- `GET /api/v1/sync/android/changes`
- `GET /api/v1/sync/winget/changes`

Les paramètres `cursor` et `updatedAfter` permettent au client mobile de reprendre une synchronisation différentielle.

## Documentation API

Les contrats OpenAPI sont exposés par le backend et relayés par la gateway:

- `http://localhost:8081/v3/api-docs`
- `http://localhost:8081/swagger-ui.html`
- `http://localhost:8081/swagger-ui/`

Le frontend Web consomme ensuite les contrats générés via RTK Query.

## Sécurité et contrôle d'accès

- authentification OAuth2 / JWT portée par la gateway
- rate limiting Bucket4j appliqué sur le point d'entrée HTTP
- le backend reste derrière la gateway pour les clients Web et mobiles

## Flux de données

### Web

`data-web` appelle la gateway pour:

- les listes CRUD
- les imports / exports
- l'impression
- les écrans techniques et de configuration

### Mobile Flutter

`flutter_application` utilise la gateway pour:

- les refresh complets (`/gtin`, `/car`, `/car-mileage`, `/android`, `/winget/list`)
- les synchronisations incrémentales (`/api/v1/sync/{domain}/changes`)
- les opérations authentifiées avec JWT ou token OIDC
