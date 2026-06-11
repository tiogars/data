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

- Module: `data-server/src/main/java/fr/tiogars/data/common/csv/CsvSupport.java`
- Capacites communes:
	- detection du delimiteur (`,` ou `;`)
	- parsing CSV (quotes, CRLF/LF, lignes vides)
	- echappement CSV standard
	- acces securise aux colonnes (`valueAt`)
	- normalisation d'en-tetes (mode strict ou permissif)
- Extensions prevues:
	- `escapeCsv(value, additionalQuoteTriggers...)` pour besoins metier (ex: `|`)
	- `normalizeHeader(value, permissive)` pour en-tetes heterogenes

### Matrice de migration P2 (CSV)

| Domaine | Statut | Details |
|---|---|---|
| GTIN | Migre | `GtinImportExportService` utilise `CsvSupport` |
| Android | Migre | `AndroidImportExportService` utilise `CsvSupport` avec options Android (`|`, en-tetes permissifs) |
| Domaines restants | Aucun identifie | Pas d'autre service CSV duplique detecte a date |

### Regle d'evolution

Pour tout nouveau domaine CSV:

1. Reutiliser `CsvSupport` en premier choix.
2. Ajouter les adaptations metier via options (sans forker la logique CSV de base).
3. Valider la non-regression via tests d'integration import/export du domaine.