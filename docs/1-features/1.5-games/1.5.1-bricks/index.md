# Bricks - Collection de briques

La rubrique **Bricks** permet de gérer une collection personnelle de briques (LEGO ou autres), avec catalogage, recherche et liens de référence.

## Objectif et périmètre

Les **bricks** sont des unités de construction individuelles. Cette feature vous permet de :

- Cataloguer votre collection complète
- Organiser par tags (thème, couleur, taille, etc.)
- Stocker des images pour chaque brique
- Consulter via des moteurs de recherche externes (BrickLink, BrickSet, etc.)
- Exporter/importer votre collection pour sauvegarde ou partage

Cette documentation couvre l'usage de la collection de bricks dans l'interface web.

Hors périmètre actuel :

- Synchronisation mobile Android
- Liens fonctionnels avec les autres collections du produit
- Gestion avancée d'inventaire pièce par pièce

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

## Captures d'écran

Les captures ci-dessous illustrent un scénario de démonstration cohérent avec l'interface Bricks.

### Liste desktop

![Liste des bricks sur desktop avec recherche, pagination et actions rapides.](images/bricks-list-desktop.png)

Liste des bricks sur desktop avec recherche, actions d'import/export, tableau et pagination.

### Vue mobile

![Vue mobile des bricks avec affichage en cartes et action d'ajout.](images/bricks-list-mobile.png)

Vue mobile de la collection avec cartes, recherche et bouton d'ajout flottant.

### Formulaire de création

![Formulaire de création d'une brick avec numéro, titre, tags et aperçu d'image.](images/bricks-form.png)

Exemple de formulaire de création ou de modification d'une brick.

### Paramétrage des liens externes

![Paramétrage des liens externes avec état actif ou inactif et actions de gestion.](images/bricks-external-links.png)

Gestion des moteurs de recherche externes utilisables depuis la feature Bricks.

## Parcours utilisateur et opérations courantes

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

## Règles métier et validations

### Champs et contraintes

- Le numéro doit être unique : deux bricks ne peuvent pas partager le même numéro
- Le titre et le numéro sont obligatoires
- Les tags sont séparés par des espaces ou des points-virgules dans le formulaire
- Une brick peut avoir plusieurs tags pour le classement et la recherche

### Image et formats acceptés

- L'image doit être au format PNG, JPEG, GIF ou WebP
- Taille maximale : 5 MB
- Résolution recommandée : 400 × 400 px ou plus
- Les images sont stockées en base64 pour rester transportables dans les exports

### Effets importants

- La suppression est définitive et ne peut pas être annulée
- Un import peut remplacer les bricks existantes qui portent le même numéro
- Le numéro est l'identifiant métier de référence de la collection

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

## Responsive : desktop et mobile

L'interface conserve les mêmes informations principales sur desktop et mobile, avec une présentation adaptée.

### Desktop

- Vue en tableau avec pagination
- Actions accessibles directement dans la ligne
- Bouton d'ajout visible dans la page de liste

### Mobile

- Vue en cartes à la place du tableau
- Bouton flottant pour l'ajout
- Suppression possible par balayage ou via l'action dédiée

### Point d'attention

- Le parcours recommandé reste : liste, détail, puis action de modification ou suppression

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

## Aspects techniques utiles

- Les exports incluent à la fois les bricks et les liens externes configurés
- Les imports acceptent JSON et CSV avec un aperçu avant confirmation
- Le PDF catalogue génère une page dédiée par brick pour l'impression ou l'archivage

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

### Modèles utiles

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

## Limites et intégration avec d'autres features

- **Mobile (Android)** : bricks ne sont pas encore synchronisées sur l'app mobile (phase future)
- **Cave (vins)** : collections indépendantes, pas de lien direct
- **Cars** : collections indépendantes, pas de lien direct

## Bonnes pratiques

1. **Sauvegardez régulièrement** : exportez votre collection en JSON tous les mois
2. **Utilisez des tags cohérents** : restez avec une convention (ex: toujours `star-wars` et jamais `starwars`)
3. **Versionnez vos exports** : mettez la date dans le nom du fichier (ex: `bricks-20240624.json`)
4. **Complétez les images** : une collection photographiée est plus facile à naviguer
5. **Testez les liens externes** : vérifiez les URLs des moteurs de recherche
