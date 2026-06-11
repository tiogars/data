# Documentation

## Stack technique

Exemples :

- pnpm
- vite
- react
- java
- spring

## Types de services

Exemples :

- api
- web
- gateway
- database

## Note technique: CsvSupport (P2)

Le backend partage maintenant un module CSV commun pour reduire la duplication entre domaines:

- Module: data-server/src/main/java/fr/tiogars/data/common/csv/CsvSupport.java
- Capacites communes:
	- detection du delimiteur (`,` ou `;`)
	- parsing CSV (quotes, CRLF/LF, lignes vides)
	- echappement CSV standard
	- acces securise aux colonnes (`valueAt`)
	- normalisation d'en-tetes (mode strict ou permissif)
- Extensions prevues:
	- escapeCsv(value, additionalQuoteTriggers...) pour besoins metier (ex: `|`)
	- normalizeHeader(value, permissive) pour en-tetes heterogenes

## Architecture Import/Export (SRP)

Les flux import/export sont desormais decoupes par responsabilite:

- 1 service JSON export par domaine: DomainExportService
- 1 service JSON import par domaine: DomainImportService
- 1 service CSV export par domaine: DomainExportCsvService
- 1 service CSV import par domaine: DomainImportCsvService
- controllers separes pour import/export et json/csv

Cette decomposition evite les classes monolithiques de type ImportExportService et applique le principe de responsabilite unique.

### Matrice de migration P2 (CSV)

| Domaine | Statut | Details |
|---|---|---|
| GTIN | Migre | Services dedies: GtinExportCsvService et GtinImportCsvService |
| Android | Migre | Services dedies: AndroidExportCsvService et AndroidImportCsvService |
| Brand | Migre | Services dedies: BrandExportCsvService et BrandImportCsvService |
| Model | Migre | Services dedies: ModelExportCsvService et ModelImportCsvService |
| Brick | Migre | Services dedies: BrickExportCsvService et BrickImportCsvService |
| UrlManager | Migre | Services dedies: UrlManagerExportCsvService et UrlManagerImportCsvService |

### Regle d'evolution

Pour tout nouveau domaine CSV:

1. Reutiliser CsvSupport en premier choix.
2. Ajouter les adaptations metier via options (sans forker la logique CSV de base).
3. Exposer les endpoints dedies: /domain/export/csv et /domain/import/csv.
4. Valider la non-regression via tests d'integration import/export du domaine.