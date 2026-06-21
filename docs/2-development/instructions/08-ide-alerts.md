# 08 - Revue des Alertes IDE

## Objectif

Standardiser la gestion des alertes remontees par l'IDE (VS Code):
- erreurs de compilation
- avertissements de lint/analyse statique
- alertes de dependances/vulnerabilites
- faux positifs et diagnostics obsoletes en cache

L'objectif est de corriger ce qui a un impact reel, de reduire le bruit, et de garder un projet compilable et maintenable.

---

## Sources d'alertes a traiter

- Problems panel VS Code
- Diagnostics du langage (TypeScript, Dart, Java)
- Lint/format (ESLint, analyse Flutter, checks Maven)
- Alertes de securite dependances (Maven/npm)

Toujours prioriser les alertes visibles dans l'IDE avant les optimisations non demandees.

---

## Priorisation (ordre obligatoire)

1. **Bloquant build/run/test**
   - Erreurs de compilation
   - Imports/modules manquants
   - Incompatibilites de versions qui cassent le build

2. **Risque fonctionnel ou securite**
   - Vulnerabilites de dependances (HIGH/CRITICAL)
   - Warnings indiquant un comportement incorrect probable

3. **Qualite et dette technique**
   - Warnings de style/types non bloquants
   - Nettoyage de code sans impact runtime

---

## Workflow de revue

1. Lister les alertes actives par fichier.
2. Grouper par type de probleme (meme cause racine).
3. Corriger d'abord les causes racines, pas les symptomes repetes.
4. Revalider immediatement:
   - diagnostics IDE
   - build/test ciblés sur les zones modifiees
5. Documenter les exceptions restantes (faux positif, action differree).

Ne pas faire de refactor global non necessaire pour une alerte locale.

---

## Regles par technologie du repo

### Java / Maven (`data-server`, `data-gateway`)

- Verifier la compatibilite Spring Boot/Spring Cloud avant modification.
- Preferer upgrade patch/minor compatible plutot que downgrade majeur, sauf contrainte explicite.
- Valider par module:
  - `./data-gateway/mvnw.cmd -f data-gateway/pom.xml -DskipTests compile`
  - `./data-server/mvnw.cmd -f data-server/pom.xml -DskipTests compile`

Si l'IDE affiche encore une ancienne alerte apres correction, considerer un cache de diagnostics et recharger le projet Java.

### Frontend TypeScript (`data-web`)

- Corriger d'abord les erreurs type-check avant les warnings ESLint.
- Ne jamais modifier manuellement les fichiers generes `src/services/*Api.ts`.
- Regenerer si necessaire:
  - `pnpm -C data-web run openapi:pull`
  - `pnpm -C data-web run rtk:codegen`

### Flutter (`flutter_application`)

- Prioriser les erreurs d'analyse Dart visibles dans l'IDE.
- Eviter les `const` invalides avec objets non constants (ex: `DateTime(...)`).
- En cas d'erreur outillage Windows (fichier verrouille), distinguer un probleme environnemental d'un vrai echec de code.

---

## Gestion des faux positifs

Une alerte peut etre classee faux positif seulement si:
- le build cible est vert,
- le diagnostic est incoherent avec le code courant,
- et la cause probable (cache IDE, index stale, extension) est identifiee.

Dans ce cas:
- noter clairement que l'alerte est non bloquante,
- proposer une action de rafraichissement (reload projet, reindex, restart language server),
- ne pas introduire de changement inutile dans le code.

---

## Critere de fin de tache

La revue des alertes IDE est consideree complete quand:
- toutes les erreurs bloquantes visibles sont corrigees,
- les warnings critiques/securite ont une action appliquee ou planifiee,
- les validations ciblées (build/tests) sont executees avec succes,
- les alertes restantes sont explicitees (et justifiees si faux positifs).
