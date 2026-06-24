# Bricks - Collection de briques

La rubrique **Bricks** permet de gérer une collection personnelle de briques (LEGO ou autres), avec catalogage, recherche et liens de référence.

## Que sont les bricks ?

Les **bricks** sont des unités de construction individuelles. Cette feature vous permet de :

- Cataloguer votre collection complète
- Organiser par tags (thème, couleur, taille, etc.)
- Stocker des images pour chaque brique
- Consulter via des moteurs de recherche externes (BrickLink, BrickSet, etc.)
- Exporter/importer votre collection pour sauvegarde ou partage

## Interface Web

### Pages disponibles

| Page | Chemin | Description |
|---|---|---|
| Liste des bricks | `/brick` | Vue paginée avec filtrage et actions |
| Détail d'une brick | `/brick/:id` | Affichage complet avec actions (modifier, supprimer, partager) |
| Paramétrage | `/brick/settings/external-links` | Gestion des liens de recherche externes |

### Parcours utilisateur standard

```
Liste (/brick) 
  ↓
Consulter détail (/brick/:id)
  ↓
Modifier ou Supprimer
```

## Opérations courantes

### Consulter votre collection

1. Accédez à `/brick` pour voir toutes les bricks
2. Sur desktop : tableau avec pagination
3. Sur mobile : cartes avec navigation swipe
4. Filtrez par tags ou recherchez par titre/numéro

### Ajouter une brick

**Via le formulaire :**

1. Sur la liste, cliquez **+ Ajouter** (desktop) ou bouton flottant (mobile)
2. Remplissez le formulaire :
   - **Numéro** : identifiant unique (ex: `60284`, `75192`) — ce champ est obligatoire
   - **Titre** : nom de la set/collection (ex: *Le camion de chantier*)
   - **Tags** : classification (ex: `city`, `truck`, `2024`, `voiturette`)
   - **Image** : cliquez pour uploader une photo de la brique
3. Cliquez **Ajouter**

**Validations :**
- Le numéro doit être unique (deux bricks ne peuvent avoir le même numéro)
- Le titre et le numéro sont obligatoires
- Les tags sont séparés par des espaces ou des points-virgules
- L'image doit être au format PNG, JPEG, GIF ou WebP (taille max : 5 MB)

### Modifier une brick

1. Consultez le détail (cliquez **Voir** depuis la liste)
2. Cliquez **Modifier** (ou icône d'édition)
3. Mettez à jour les champs selon vos besoins
4. Cliquez **Enregistrer**

### Supprimer une brick

**Depuis la liste :**
- Desktop : cliquez l'icône ✕ dans la rangée
- Mobile : balayez à gauche ou cliquez **Supprimer**

**Depuis le détail :**
1. Cliquez **Supprimer**
2. Confirmez dans la boîte de dialogue

⚠️ **Attention** : la suppression est définitive et ne peut pas être annulée. Les données ne seront pas sauvegardées dans les exports suivants.

### Rechercher et filtrer

- **Barre de recherche** : tapez pour filtrer par titre ou numéro
- **Tags** : cliquez sur un tag dans le détail pour voir d'autres bricks avec le même tag
- **Pagination** : navigez avec les flèches ou sélectionnez une taille de page (10, 20, 50)

### Générer un PDF catalogue

1. Sur la liste, cliquez **PDF** (icône 📄)
2. Le catalogue complet se télécharge en PDF
3. Chaque brick a une page dédiée avec titre, numéro, tags et image
4. Idéal pour imprimer ou conserver un backup visuel

## Import / Export

### Exporter votre collection

Deux formats de sortie :

#### JSON

1. Cliquez **Export JSON** sur la liste
2. Fichier `bricks-YYYYMMDD.json` téléchargé

Format JSON :
```json
{
  "bricks": [
    {
      "number": "60284",
      "title": "Le camion de chantier",
      "tags": ["city", "truck"],
      "imageBase64": "data:image/png;base64,iVBORw0KGgo..."
    },
    {
      "number": "75192",
      "title": "X-Wing Starfighter",
      "tags": ["star-wars", "ship"]
    }
  ],
  "externalLinks": [
    {
      "name": "BrickLink",
      "url": "https://www.bricklink.com/v2/search.page?q=",
      "enabled": true
    }
  ]
}
```

#### CSV

1. Cliquez **Export CSV** sur la liste
2. Fichier `bricks-YYYYMMDD.csv` téléchargé

Format CSV :
```csv
type,brickNumber,brickTitle,brickTags,brickImageBase64,linkName,linkUrl,linkEnabled
brick,60284,Le camion de chantier,city|truck,data:image/png;base64,iVBORw0KGgo...,,,
brick,75192,X-Wing Starfighter,star-wars|ship,,,
external-link,,,,,BrickLink,https://www.bricklink.com/v2/search.page?q=,true
```

### Importer une collection

1. Cliquez **Importer** (📥 icône ou bouton) sur la liste
2. Sélectionnez votre fichier JSON ou CSV
3. Vérifiez les données dans l'aperçu
4. Cliquez **Confirmer l'import**

⚠️ **Attention** : les imports remplacent les bricks existantes avec le même numéro.

#### Format JSON attendu

```json
{
  "bricks": [
    {
      "number": "60284",
      "title": "Le camion de chantier",
      "tags": ["city", "truck"],
      "imageBase64": "data:image/png;base64,iVBORw0KGgo..."
    }
  ],
  "externalLinks": [
    {
      "name": "BrickLink",
      "url": "https://www.bricklink.com/v2/search.page?q=",
      "enabled": true
    }
  ]
}
```

#### Format CSV attendu

Colonnes requises (ordre libre) :
- `type` : `brick` ou `external-link`
- Pour type `brick` : `brickNumber`, `brickTitle`, `brickTags` (séparés par `|`), `brickImageBase64` (optionnel)
- Pour type `external-link` : `linkName`, `linkUrl`, `linkEnabled` (true/false)

## Liens externes

### Ajouter/modifier des liens de recherche

Les **liens externes** vous permettent de rechercher rapidement des bricks sur des moteurs spécialisés.

1. Allez à `/brick/settings/external-links`
2. Cliquez **+ Ajouter un lien**
3. Remplissez :
   - **Nom** : label du lien (ex: *BrickLink*)
   - **URL** : template de recherche (ex: `https://www.bricklink.com/v2/search.page?q=`)
   - **Activé** : cochez pour rendre le lien visible dans la liste
4. Cliquez **Enregistrer**

### Utiliser un lien externe

1. Consultez le détail d'une brick
2. Cliquez sur l'icône 🔗 à côté du lien
3. Une nouvelle fenêtre s'ouvre avec la brick recherchée

⚠️ Le numéro de la brick est ajouté à la fin du template URL.

## Concepts clés

### Numéro de brick

Identifiant unique assigné lors de la création. Exemples :
- LEGO : `60284`, `75192`
- Brick générique : `BK001`, `BK002`

Le numéro ne peut pas être changé après création (modifiez le détail et recréez si nécessaire).

### Tags

Mots-clés pour classifier votre collection. Utilisations courantes :
- **Thème** : `city`, `star-wars`, `friends`, `ninjago`
- **Type** : `set`, `minifigure`, `accessoire`
- **Couleur** : `red`, `blue`, `transparent`
- **Taille** : `small`, `large`, `micro`
- **État** : `neuf`, `occasion`, `restauré`
- **Année** : `2024`, `2023`, `vintage`

Un brick peut avoir plusieurs tags (ex: `city|truck|2024`).

### Image

Stockée en base64 (texte) pour permettre la portabilité dans les exports. Formats supportés :
- PNG, JPEG, GIF, WebP
- Résolution recommandée : 400×400 px ou plus
- Taille max : 5 MB

## Dépannage

### « Le numéro de brique est déjà utilisé »

Deux bricks ont le même numéro. Solutions :
- Vérifiez l'import (doublon dans le fichier CSV/JSON)
- Modifiez le numéro d'une des deux bricks
- Supprimez l'ancienne si elle est obsolète

### Les images ne s'affichent pas après import

- Vérifiez que l'image est en base64 dans le fichier JSON
- Format attendu : `data:image/png;base64,iVBORw0KGgo...`
- CSV : l'image doit être codée en base64 dans la colonne `brickImageBase64`

### L'export PDF est vide

- Vérifiez que vous avez au moins une brick
- Si vide, une page blanche est générée (comportement attendu)

## Intégration avec d'autres features

- **Mobile (Android)** : bricks ne sont pas encore synchronisées sur l'app mobile (phase future)
- **Cave (vins)** : collections indépendantes, pas de lien direct
- **Cars** : collections indépendantes, pas de lien direct

## API Reference

### Endpoints disponibles

| Méthode | Endpoint | Description |
|---|---|---|
| GET | `/brick/list` | Lister toutes les bricks |
| POST | `/brick` | Créer une brick |
| GET | `/brick/:id` | Récupérer une brick |
| PUT | `/brick/:id` | Mettre à jour une brick |
| DELETE | `/brick/:id` | Supprimer une brick |
| DELETE | `/brick/all` | Supprimer toutes les bricks |
| GET | `/brick/export` | Exporter en JSON |
| POST | `/brick/import` | Importer depuis JSON |
| GET | `/brick/export/csv` | Exporter en CSV |
| POST | `/brick/import/csv` | Importer depuis CSV |
| GET | `/brick/catalog/pdf` | Générer le catalogue PDF |

### Modèles

**Brick :**
```json
{
  "id": "uuid",
  "number": "60284",
  "title": "Le camion de chantier",
  "tags": ["city", "truck"],
  "imageBase64": "data:image/png;base64,..." (optionnel),
  "createdAt": "2024-06-24T10:00:00Z",
  "updatedAt": "2024-06-24T10:00:00Z"
}
```

**External Link :**
```json
{
  "id": "uuid",
  "name": "BrickLink",
  "url": "https://www.bricklink.com/v2/search.page?q=",
  "enabled": true
}
```

## Bonnes pratiques

1. **Sauvegardez régulièrement** : exportez votre collection en JSON tous les mois
2. **Utilisez des tags cohérents** : restez avec une convention (ex: toujours `star-wars` et jamais `starwars`)
3. **Versionnez vos exports** : mettez la date dans le nom du fichier (ex: `bricks-20240624.json`)
4. **Complétez les images** : une collection photographiée est plus facile à naviguer
5. **Testez les liens externes** : vérifiez les URLs des moteurs de recherche
