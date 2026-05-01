# Data

Data est un outil pour gérer de la donnée.

## Links

- [http://localhost:8080](http://localhost:8080)
- [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health)
- [http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)
- [http://localhost:3000/](http://localhost:3000/)
- [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- [http://localhost:5173/](http://localhost:5173/)
- [http://localhost:8000/data/](http://localhost:8000/data/)

## Documentation

```bash
docker compose -f '.\docker-compose.yml' up --build --watch
```

## Web

```bash
pnpm install
pnpm dev
pnpm build
pnpm run generate:section-api
pnpm upgrade -i --latest
pnpm add @mui/react-data-grid
```

## API Client Generation (data-web)

Les services API frontend sont générés à partir de la spec OpenAPI backend.

- Ne pas modifier manuellement les fichiers générés.
- Fichier généré principal: data-web/src/services/sectionApi.ts
- Exécuter depuis data-web:

```bash
pnpm run openapi:pull
pnpm run rtk:codegen:section
```

Pipeline complet:

```bash
pnpm run generate:section-api
```

## Server

- [https://www.baeldung.com/spring-rest-openapi-documentation](https://www.baeldung.com/spring-rest-openapi-documentation)
