# DEVELOPMENT.md - Guide de Démarrage pour les Développeurs

Bienvenue dans le projet **data**! Ce guide vous aidera à comprendre les règles, patterns et conventions utilisés dans ce monorepo.

## 📚 Documentation de Développement

Toutes les instructions de développement sont centralisées dans [docs/2-development/instructions/](docs/2-development/instructions/).

### Sections Clés

| Section | Description | Lire en premier |
|---------|-----------|--------|
| **[00 - Architecture](docs/2-development/instructions/00-architecture.md)** | Structure du monorepo, organisation des features, conventions de nommage | ✓ OUI |
| **[01 - Frontend](docs/2-development/instructions/01-frontend.md)** | React, MUI, Redux, RTK Query, formulaires, responsive design | Si vous codez du React |
| **[02 - Backend](docs/2-development/instructions/02-backend.md)** | Spring Boot, services, repositories, validation, OpenAPI | Si vous codez du Java |
| **[03 - Testing](docs/2-development/instructions/03-testing.md)** | Conventions de tests, couverture, nommage | Pour tous |
| **[04 - Documentation](docs/2-development/instructions/04-documentation.md)** | Javadoc, commentaires, OpenAPI, MkDocs | Pour la documentation |
| **[05 - Sécurité](docs/2-development/instructions/05-security.md)** | Validation, authentification, prévention XSS, logging | Pour tous |
| **[06 - Améliorations](docs/2-development/instructions/06-improvements.md)** | Propositions futures et évolutions techniques | Optionnel, pour planification |

## 🚀 Démarrage Rapide

### 1. Cloner le Repository
```bash
git clone https://github.com/tiogars/data.git
cd data
```

### 2. Lancer l'Environnement Local
```bash
docker compose up --build --watch
```

Accédez à :
- **Frontend:** http://localhost:5173
- **API Gateway:** http://localhost:8081
- **API Server:** http://localhost:8080
- **Documentation:** http://localhost:8000/data/
- **Monitoring:** http://localhost:3000 (Grafana)

### 3. Ou Lancer les Modules Séparément

**Frontend (React):**
```bash
pnpm -C data-web install
pnpm -C data-web dev
```

**Backend (Spring Boot):**
```bash
./data-server/mvnw.cmd -f data-server/pom.xml spring-boot:run
./data-gateway/mvnw.cmd -f data-gateway/pom.xml spring-boot:run
```

## 📋 Workflow pour Ajouter une Nouvelle Fonctionnalité

Voir [Ajouter une Nouvelle Fonctionnalité](docs/2-development/instructions/index.md#how-to-implement-a-new-feature) pour le workflow complet.

**TL;DR:**
1. Créer backend (entités, services, tests)
2. Générer API frontend (`pnpm -C data-web run openapi:pull && pnpm -C data-web run rtk:codegen`)
3. Créer frontend (pages, composants, tests)
4. Documenter dans MkDocs si nécessaire
5. Créer une PR

## 🔑 Principes Clés

### 1. Responsive by Default
Tous les UIs doivent fonctionner sur mobile et desktop.

### 2. API-Centric Design
Le backend définit les contrats API. Le frontend consomme les APIs générées automatiquement.

### 3. Séparation des Responsabilités
- Backend: Controllers → Services → Repositories
- Frontend: Pages → Components → Hooks

### 4. Type Safety
TypeScript strict côté frontend, Java génériques côté backend.

### 5. Tests en Premier
Les tests ne sont pas optionnels. Couverture cible: ≥80% services, ≥70% global.

### 6. Documentation Partout
Javadoc, commentaires, OpenAPI, MkDocs. Maintenez la doc à jour avec le code!

## 🛠 Conventions Importantes

### Nommage
- **Java:** PascalCase pour les classes (`BrickCreationService`)
- **React:** PascalCase pour les composants (`BrickForm.tsx`)
- **Fonctions:** camelCase (`useBrickList()`)
- **Bases de données:** snake_case (`external_link`)
- **Packages:** lowercase, basé sur le domaine (`fr.tiogars.data.dev.docs.brick.services`)

### Langues
- **Code:** Anglais
- **Javadoc/Comments:** Français
- **Descriptions OpenAPI:** Français
- **Commits:** Anglais ou Français (flexible)

### Structure des Features
Les features sont organisées par **domaine** (ex: `brick`, `section`, `footerLink`):

```
data-server/src/main/java/fr/tiogars/data/dev/docs/<domain>/
├── controllers/
├── services/
├── repositories/
├── entities/
├── models/
├── forms/

data-web/src/
├── pages/<domain>/
├── components/<domain>/
├── features/<domain>/
└── services/<domain>Api.ts (généré)
```

## 📝 Avant de Pousser du Code

- [ ] Tests écrits et passants (`mvn clean test` et `pnpm test`)
- [ ] Pas de violations de linting (`pnpm lint`)
- [ ] Javadoc sur les classes/méthodes publiques (frontend: TSDoc)
- [ ] OpenAPI annotations complètes sur les endpoints
- [ ] Code responsive (mobile-first)
- [ ] Pas de secrets en dur dans le code
- [ ] Documentation mise à jour si nécessaire

## 🐛 Dépannage Courant

### Problème: "API changed but tests fail"
**Solution:** Régénérer les APIs frontend:
```bash
pnpm -C data-web run openapi:pull
pnpm -C data-web run rtk:codegen
```

### Problème: "Port déjà utilisé"
**Solution:** Vérifiez les processus existants:
```bash
lsof -i :5173    # Frontend
lsof -i :8081    # Gateway
lsof -i :8080    # API Server
```

### Problème: "Tests fail locally"
**Solution:** Nettoyer et relancer:
```bash
mvn clean test   # Backend
pnpm -C data-web test   # Frontend
```

## 🆘 Besoin d'Aide?

### Questions sur l'Architecture?
Voir [00 - Architecture](docs/2-development/instructions/00-architecture.md)

### Questions Frontend?
Voir [01 - Frontend Patterns](docs/2-development/instructions/01-frontend.md)

### Questions Backend?
Voir [02 - Backend Patterns](docs/2-development/instructions/02-backend.md)

### Questions Sécurité?
Voir [05 - Security Practices](docs/2-development/instructions/05-security.md)

## 📖 Ressources Supplémentaires

- [README Principal](README.md)
- [Architecture Système](docs/3-system/index.md)
- [Documentation Utilisateur](docs/1-features/)
- [Instructions Complètes](docs/2-development/instructions/)

## 💡 Contribution

1. Fork le repository
2. Créez une branche pour votre feature (`git checkout -b feature/something`)
3. Suivez les patterns et conventions documentés
4. Écrivez des tests
5. Committez (`git commit -am 'Add feature'`)
6. Poussez la branche (`git push origin feature/something`)
7. Créez une Pull Request

---

**Bon codage!** 🚀

Pour toute question sur les patterns ou conventions, consultez d'abord la documentation dans [docs/2-development/instructions/](docs/2-development/instructions/).
