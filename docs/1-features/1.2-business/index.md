# UX Web

Cette section documente l'expérience utilisateur actuellement implémentée dans `data-web`.

## Principes UX

- navigation par domaine avec pages dédiées
- parcours standards: liste -> détail -> création / édition
- rendu responsive: table sur desktop, cartes ou listes sur mobile
- formulaires cohérents avec MUI et validation côté API
- accès direct à la documentation et au support depuis l'interface

## Parcours communs

### Référentiels CRUD

Les domaines de référence suivent une structure homogène:

- `/domain/list` pour consulter et filtrer
- `/domain/create` pour créer
- `/domain/:id` pour consulter le détail
- `/domain/:id/edit` pour modifier

Domaines suivant ce schéma:

- Section
- Footer Link
- Menu Item
- GTIN
- Android
- Winget
- Brand
- Model
- Continent
- User Account

### Parcours spécifiques

Certains domaines ont une UX dédiée:

| Domaine | Parcours web |
|---|---|
| GitHub Repository | recherche (`/github-repository/search`), création, détail, édition |
| GitHub Token Config | page de configuration (`/github-token-config/search`) |
| Url Manager | gestion centralisée des URLs (`/url-manager`) et des cartes (`/url-cards`) |
| Brick | liste, détail, et paramétrage des liens externes (`/1.5-games/1.5.1-bricks/`) — [voir doc complète](../1.5-games/1.5.1-bricks/index.md) |
| Car | tableau de bord (`/car/dashboard`), liste, création, édition |
| Car Mileage | vue tableau (`/car-mileage/table`) et formulaire dédié (`/car-mileage/form`) |
| Android | liste principale et vue des releases (`/android/releases`) |

## Responsive et impression

Les listes Web suivent les conventions suivantes:

- desktop (`md+`): affichage tabulaire pour les données denses
- mobile (`xs/sm`): affichage carte ou liste avec les mêmes informations clés
- impression des listes quand le domaine le supporte côté backend:
  - mode `filtered`
  - mode `all`
- génération côté client de formulaires vierges pour les flux de saisie papier quand la page le prévoit

Exemples déjà implémentés:

- impression de listes: Android, Model
- formulaires vierges: Car Mileage

## Navigation transverse

Pages non métier disponibles dans l'application:

- `/` : accueil
- `/gateway-config` : configuration de l'URL de gateway
- `/auth-config` : configuration de l'URL d'authentification
- `/auth/account` : compte OIDC courant
- `/server-info/java-version` : version Java du backend
- `/server-info/jpa-entities` : inventaire des entités JPA
- `/icon-gallery` : galerie d'icônes

## Authentification

L'application Web s'appuie sur OIDC:

- callback de connexion: `/auth/callback`
- callback de déconnexion: `/auth/logout-callback`
- consultation du compte connecté: `/auth/account`

## Aide à la navigation

- l'en-tête expose un lien direct vers la documentation MkDocs
- le support ouvre la création d'issue GitHub
- les breadcrumbs et la barre latérale structurent la navigation entre domaines
