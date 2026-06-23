# Cave - Collection de vins

La rubrique **Cave** permet de gérer une collection de vins dégustés, avec tous les domaines de référence associés.

## Domaines

### Domaines de référence

| Domaine | Description | Chemin API | Page web |
|---|---|---|---|
| Appellation | Appellations vinicoles (Champagne, Bordeaux, Bourgogne…) | `/appellation` | `/appellation/list` |
| Couleur | Couleurs de vin (Blanc, Rouge, Rosé) | `/couleur` | `/couleur/list` |
| Type de vin | Types de vin (Brut, Blanc de blancs, Rosé…) | `/type-vin` | `/type-vin/list` |
| Cépage | Cépages (Chardonnay, Pinot noir, Pinot meunier…) | `/cepage` | `/cepage/list` |
| Contenant | Contenants avec volume en cl (Bouteille, Magnum…) | `/contenant` | `/contenant/list` |
| Circonstance | Circonstances de dégustation (Anniversaire, Mariage…) | `/circonstance` | `/circonstance/list` |
| Maison | Maisons vinicoles avec site web | `/maison` | `/maison/list` |
| Nom de vin | Noms de vins associés à une maison | `/vin-nom` | `/vin-nom/list` |
| Tag vin | Tags libres pour les vins | `/vin-tag` | `/vin-tag/list` |

### Domaine principal

| Domaine | Description | Chemin API | Page web |
|---|---|---|---|
| Vin | Fiche complète d'un vin dégusté | `/vin` | `/vin/list` |

## Fonctionnalités

Chaque domaine dispose de :

- **CRUD** : création, lecture, modification, suppression (unitaire et globale)
- **Recherche paginée** : `GET /{domain}/search?page=0&size=10&q=`
- **Export JSON** : `GET /{domain}/export`
- **Import JSON** : `POST /{domain}/import`
- **Export CSV** : `GET /{domain}/export/csv`
- **Import CSV** : `POST /{domain}/import/csv`
- **Impression** : `GET /{domain}/print?mode=filtered|all`
- **Interface web** : liste, fiche, création, modification

## Fiche Vin

La fiche d'un vin contient :

- **Référence** : appellation, couleur, type de vin, maison, nom du vin, contenant
- **Géographie** : année, commune, région
- **Cépages** : liste des cépages avec pourcentage d'assemblage
- **Circonstances** : liste des circonstances de dégustation
- **Tags** : tags libres
- **Dégustation** : commentaires, accords mets & vins
- **Photos** : stockage de photos associées

## Contrat API Import / Export

| Domaine | Export JSON | Import JSON | Export CSV | Import CSV |
|---|---|---|---|---|
| Appellation | `GET /appellation/export` | `POST /appellation/import` | `GET /appellation/export/csv` | `POST /appellation/import/csv` |
| Couleur | `GET /couleur/export` | `POST /couleur/import` | `GET /couleur/export/csv` | `POST /couleur/import/csv` |
| Type de vin | `GET /type-vin/export` | `POST /type-vin/import` | `GET /type-vin/export/csv` | `POST /type-vin/import/csv` |
| Cépage | `GET /cepage/export` | `POST /cepage/import` | `GET /cepage/export/csv` | `POST /cepage/import/csv` |
| Contenant | `GET /contenant/export` | `POST /contenant/import` | `GET /contenant/export/csv` | `POST /contenant/import/csv` |
| Circonstance | `GET /circonstance/export` | `POST /circonstance/import` | `GET /circonstance/export/csv` | `POST /circonstance/import/csv` |
| Maison | `GET /maison/export` | `POST /maison/import` | `GET /maison/export/csv` | `POST /maison/import/csv` |
| Nom de vin | `GET /vin-nom/export` | `POST /vin-nom/import` | `GET /vin-nom/export/csv` | `POST /vin-nom/import/csv` |
| Tag vin | `GET /vin-tag/export` | `POST /vin-tag/import` | `GET /vin-tag/export/csv` | `POST /vin-tag/import/csv` |
| Vin | `GET /vin/export` | `POST /vin/import` | — | — |

## Architecture

### Backend (`data-server`)

```
data-server/src/main/java/fr/tiogars/data/cave/
├── appellation/   # Appellations vinicoles
├── cepage/        # Cépages
├── circonstance/  # Circonstances de dégustation
├── contenant/     # Contenants
├── couleur/       # Couleurs de vin
├── maison/        # Maisons vinicoles
├── typevin/       # Types de vin
├── vin/           # Vins dégustés (domaine principal)
├── vinnom/        # Noms de vins
└── vintag/        # Tags de vins
```

Chaque sous-domaine suit le même pattern :
- `controllers/` — contrôleurs REST (un par opération)
- `entities/` — entités JPA
- `forms/` — formulaires de création/import
- `menu/` — contribution au menu (rubrique Cave)
- `models/` — modèles de réponse (DTOs)
- `repositories/` — repositories Spring Data JPA
- `services/` — services métier (un par opération)

### Gateway (`data-gateway`)

Les routes Cave sont exposées via `GatewayRoutesConfiguration` :
`/appellation`, `/couleur`, `/circonstance`, `/contenant`, `/type-vin`, `/cepage`, `/maison`, `/vin-nom`, `/vin-tag`, `/vin`

### Frontend (`data-web`)

```
data-web/src/
├── services/
│   ├── appellationApi.ts
│   ├── cepageApi.ts
│   ├── circonstanceApi.ts
│   ├── contenantApi.ts
│   ├── couleurApi.ts
│   ├── maisonApi.ts
│   ├── typeVinApi.ts
│   ├── vinApi.ts
│   ├── vinNomApi.ts
│   └── vinTagApi.ts
└── pages/
    ├── appellation/    # List, Create, Detail, Edit
    ├── cepage/
    ├── circonstance/
    ├── contenant/
    ├── couleur/
    ├── maison/
    ├── typeVin/
    ├── vin/
    ├── vinNom/
    └── vinTag/
```

## Tests

Des tests d'intégration sont disponibles pour :
- `AppellationApiIntegrationTest`
- `ContenantApiIntegrationTest`
- `MaisonApiIntegrationTest`
- `VinApiIntegrationTest`
