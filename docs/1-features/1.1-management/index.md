# Management

- Création
- Edition
- Impression
- Import
- Export
- Formats : csv, json, xml
- Recherche
- Sélection

## Convention Import / Export

Les operations sont exposees avec des routes dediees:

- Export JSON: GET /domain/export
- Import JSON: POST /domain/import
- Export CSV: GET /domain/export/csv
- Import CSV: POST /domain/import/csv

Exemples de domaines de management:

- /gtin
- /android
- /brand
- /model
- /brick
- /url-manager

## Convention Impression

Pour les domaines avec affichage de listes, l'impression suit:

- GET /domain/print
- Parametre mode: filtered ou all
- Metadonnees de sortie: generatedAt et total

## Contrat API Import / Export

### Tableau des endpoints

| Domaine | Export JSON | Import JSON | Export CSV | Import CSV |
|---|---|---|---|---|
| GTIN | GET /gtin/export | POST /gtin/import | GET /gtin/export/csv | POST /gtin/import/csv |
| Android | GET /android/export | POST /android/import | GET /android/export/csv | POST /android/import/csv |
| Brand | GET /brand/export | POST /brand/import | GET /brand/export/csv | POST /brand/import/csv |
| Model | GET /model/export | POST /model/import | GET /model/export/csv | POST /model/import/csv |
| Brick | GET /brick/export | POST /brick/import | GET /brick/export/csv | POST /brick/import/csv |
| UrlManager | GET /url-manager/export | POST /url-manager/import | GET /url-manager/export/csv | POST /url-manager/import/csv |

### Exemples GTIN

Import JSON:

```json
{
	"items": [
		{ "code": "0123456789012", "description": "Produit A" },
		{ "code": "9780201379624", "description": "Produit B" }
	]
}
```

Import CSV:

```csv
code,description
0123456789012,Produit A
9780201379624,Produit B
```

### Exemples Android

Import JSON:

```json
{
	"items": [
		{
			"name": "Google Keep",
			"packageName": "com.google.android.keep",
			"category": ["productivity", "notes"],
			"description": "Application de prise de notes",
			"icon": "https://example.org/keep.png"
		}
	]
}
```

Import CSV:

```csv
name,packageName,category,description,icon
Google Keep,com.google.android.keep,productivity|notes,Application de prise de notes,https://example.org/keep.png
```

### Exemples Brand

Import JSON (format text):

```json
{
	"text": "Lego\nMattel\nHasbro"
}
```

Import JSON (format historique):

```json
{
	"items": [
		{ "name": "Lego", "description": "Jeux de construction" },
		{ "name": "Mattel", "description": "Jouets" }
	]
}
```

Import CSV:

```csv
name,description
Lego,Jeux de construction
Mattel,Jouets
```

### Exemples Model

Import JSON:

```json
{
	"items": [
		{
			"name": "Modele Catalogue",
			"description": "Structure produit",
			"modelAttributes": [
				{ "name": "size", "description": "Taille" },
				{ "name": "weight", "description": "Poids" }
			]
		}
	]
}
```

Import CSV (attributes = name::description separes par |):

```csv
name,description,attributes
Modele Catalogue,Structure produit,size::Taille|weight::Poids
```

### Exemples Brick

La gestion des liens externes se fait desormais via le menu: Bricks > Settings > External links.

Import JSON:

```json
{
	"bricks": [
		{
			"number": "60284",
			"title": "Le camion de chantier",
			"tags": ["city", "truck"],
			"imageBase64": "data:image/png;base64,iVBORw0KGgoAAAANSUhEUg..."
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

Import CSV (mix type=brick et type=external-link):

```csv
type,brickNumber,brickTitle,brickTags,brickImageBase64,linkName,linkUrl,linkEnabled
brick,60284,Le camion de chantier,city|truck,data:image/png;base64,iVBORw0KGgoAAAANSUhEUg...,,,
external-link,,,,,BrickLink,https://www.bricklink.com/v2/search.page?q=,true
```

### Exemples UrlManager

Import JSON:

```json
{
	"urls": [
		{
			"label": "Board Sprint",
			"url": "https://jira.exemple.fr/board/42",
			"tags": ["sprint", "team-a"],
			"description": "Tableau sprint"
		}
	],
	"cards": [
		{
			"title": "Equipe Dev",
			"tags": ["dev", "backend"],
			"matchMode": "all"
		}
	]
}
```

Import CSV (mix type=url et type=card):

```csv
type,label,url,tags,description,title,matchMode
url,Board Sprint,https://jira.exemple.fr/board/42,sprint|team-a,Tableau sprint,,
card,,,dev|backend,,Equipe Dev,all
```

## Notes de compatibilite

- Les endpoints /domain/export et /domain/import restent la reference JSON.
- Les endpoints /domain/export/csv et /domain/import/csv sont dedies aux flux fichier.
- Pour les imports CSV, le content-type accepte text/csv et text/plain.
