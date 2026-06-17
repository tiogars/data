.PHONY: help android-release-builder restart-services restart-service

help:
	@echo "Available targets:"
	@echo "  android-release-builder  Build and run Android release builder container"
	@echo "  restart-services         Restart all running docker compose services"

android-release-builder:
	docker compose --profile android-build up --build android-release-builder

restart-services:
	docker compose restart
