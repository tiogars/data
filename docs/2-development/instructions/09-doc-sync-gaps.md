# 09 - Écarts Documentation / Code

Cette section inventorie les désynchronisations entre le code du dépôt et la documentation existante (`docs/`, `.github/copilot-instructions.md`, `docs/2-development/instructions/`).

---

## 1. Chemin de package backend incorrect dans l'architecture

**Fichier concerné :** `docs/2-development/instructions/00-architecture.md`

La documentation indique :

```
data-server/src/main/java/fr/tiogars/data/dev/docs/<domain>/
```

Ce chemin n'existe pas. Les domaines sont organisés par **catégorie métier**, pas sous `dev/docs`. Chemin réel :

| Catégorie package | Domaines |
|---|---|
| `fr.tiogars.data.games` | `brick` |
| `fr.tiogars.data.products` | `gtin`, `brand` |
| `fr.tiogars.data.vehicles` | `car`, `carmileage` |
| `fr.tiogars.data.softwares` | `android` |
| `fr.tiogars.data.locations` | `continent` |
| `fr.tiogars.data.settings` | `footerlink`, `menuitem`, `urlmanager`, `useraccount` |
| `fr.tiogars.data.sync` | `sync` |
| `fr.tiogars.data.system` | `serverinfo` |
| `fr.tiogars.data.dev` | `githubrepository`, `githubrestconfig`, `model` |
| `fr.tiogars.data.docs` | `section` |

**Correction attendue :** Remplacer la structure de package fictive par le schéma réel basé sur les catégories.

---

## 2. Tableau Import/Export incomplet dans la documentation management

**Fichier concerné :** `docs/1-features/1.1-management/index.md`

Le tableau liste 6 domaines : GTIN, Android, Brand, Model, Brick, UrlManager.

Or les domaines **Car** et **CarMileage** disposent également de contrôleurs Import/Export complets dans le code :

- `CarImportController`, `CarExportController`, `CarImportCsvController`, `CarExportCsvController`
- `CarMileageImportController`, `CarMileageExportController`, `CarMileageImportCsvController`, `CarMileageExportCsvController`

**Correction attendue :** Ajouter Car et CarMileage au tableau des endpoints import/export.

---

## 3. Absence de documentation sur les domaines avec support d'impression

**Fichier concerné :** `docs/1-features/1.1-management/index.md` et `docs/2-development/instructions/02-backend.md`

La convention impression est décrite (`mode filtered / all`, `generatedAt`, `total`) mais aucun tableau n'indique quels domaines l'ont implémentée.

Domaines avec un contrôleur `Print` ou `Pdf` dans le code :

| Domaine | Contrôleur |
|---|---|
| `brand` | `BrandPrintController` |
| `android` | `AndroidPrintController` |
| `gtin` | `GtinPrintController` |
| `model` | `ModelPrintController` |
| `brick` | `BrickPdfController` |

Domaines identifiés sans contrôleur d'impression : `car`, `car-mileage`, `continent`, `section`, `url-manager`, `footer-link`, `menu-item`, `user-account`.

**Correction attendue :** Créer un tableau de couverture impression dans la documentation management.

---

## 4. Features Flutter manquantes dans la documentation mobile

**Fichier concerné :** `docs/2-development/instructions/07-mobile-flutter.md`

La documentation liste l'arborescence Flutter suivante :

```
features/
├── gtin/
├── vehicles/
└── android_apps/
```

Or le code (`flutter_application/lib/features/`) contient également :

- `auth/` (authentification)
- `dashboard/` (tableau de bord)

Ces deux features ne sont pas documentées.

**Correction attendue :** Ajouter `auth/` et `dashboard/` à l'arborescence et les décrire sommairement.

---

## 5. Instruction 07-mobile-flutter absente du fichier copilot-instructions

**Fichier concerné :** `.github/copilot-instructions.md`

La section *Quick Reference* liste les fichiers d'instructions 00 à 06 et 08 dans la table de navigation :

```markdown
- [07 - Mobile Flutter](../docs/2-development/instructions/07-mobile-flutter.md)
```

Ce lien est absent de l'index copilot-instructions, alors que le fichier `07-mobile-flutter.md` existe et contient des conventions actives.

**Correction attendue :** Ajouter le lien vers `07-mobile-flutter.md` dans la liste de navigation du fichier copilot-instructions.

---

## 6. Route gateway `/api/**` non documentée

**Fichier concerné :** `docs/2-development/instructions/00-architecture.md`, `docs/1-features/1.3-mobile/architecture-sync.md`

`GatewayRoutesConfiguration.java` expose une route catch-all `/api/**` en plus des routes par domaine. L'endpoint de synchronisation incrémentale mobile utilise cette route :

```
GET /api/v1/sync/{domain}/changes
```

Cette route n'apparaît dans aucune documentation d'architecture du gateway. Elle n'est pas non plus listée dans le tableau des routes de `copilot-instructions.md`.

**Correction attendue :** Documenter la route `/api/**` et son rôle (synchronisation mobile) dans la documentation architecture du gateway.

---

## 7. Absence de pages de documentation fonctionnelle par domaine

**Dossier concerné :** `docs/1-features/1.2-business/`

Le fichier `docs/1-features/1.2-business/index.md` liste des sujets métier de haut niveau (Achat, Vente, Santé…) qui ne sont pas du tout implémentés. À l'inverse, les domaines **implémentés** n'ont aucune page fonctionnelle :

| Domaine implémenté | Page docs/1-features/ |
|---|---|
| `gtin` | ❌ absent |
| `brand` | ❌ absent |
| `brick` | ❌ absent |
| `car` | ❌ absent |
| `car-mileage` | ❌ absent |
| `android` | ❌ absent |
| `continent` | ❌ absent |
| `section` | ❌ absent |
| `url-manager` | ❌ absent |
| `menu-item` | ❌ absent |
| `footer-link` | ❌ absent |
| `user-account` | ❌ absent |

**Correction attendue :** Créer une page de documentation fonctionnelle par domaine implémenté (description, champs, règles métier) dans `docs/1-features/1.2-business/`.

---

## 8. Structure frontend dans l'architecture doc ne reflète pas la réalité

**Fichier concerné :** `docs/2-development/instructions/00-architecture.md`

La documentation décrit une structure avec un dossier `components/<domain>/` séparé des pages, mais le code organise les composants réutilisables directement sous `data-web/src/components/` (sans sous-dossier par domaine). Les `features/` du frontend ne contiennent que des utilitaires transverses (`apiErrorSnackbar`, `footerLink`, `menuItem`, `urlManager`) et non des slices Redux par domaine comme suggéré.

**Correction attendue :** Mettre à jour la structure frontend dans la documentation pour refléter l'organisation réelle (`pages/`, `components/`, `features/` transversaux, `services/` générés).

---

## Résumé des priorités

| Priorité | Écart | Effort |
|---|---|---|
| Haute | Chemin de package backend fictif (#1) | Faible |
| Haute | Import/Export manquants pour Car et CarMileage (#2) | Faible |
| Haute | Lien 07-mobile-flutter absent de copilot-instructions (#5) | Faible |
| Moyenne | Tableau couverture impression manquant (#3) | Moyen |
| Moyenne | Features Flutter `auth/` et `dashboard/` non documentées (#4) | Moyen |
| Moyenne | Route `/api/**` gateway non documentée (#6) | Moyen |
| Basse | Structure frontend à corriger (#8) | Moyen |
| Basse | Pages fonctionnelles par domaine absentes (#7) | Élevé |
