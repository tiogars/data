.PHONY: help android-release-builder restart-services restart-service

help:
	@echo "Available targets:"
	@echo "  android-release-builder  Build and run Android release builder container"
	@echo "  restart-services         Restart all running docker compose services"
	@echo "  data-mkdocs-build        Build MkDocs documentation"

android-release-builder:
	docker compose --profile android-build up --build android-release-builder

# Restart all services and force rebuild the images
restart-services:
	docker compose -f 'docker-compose.yml' up -d --build --force-recreate

data-mkdocs-build:
	docker compose exec -T data-mkdocs mkdocs build --clean