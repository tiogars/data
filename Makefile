.PHONY: help android-release-builder restart-services restart-service db-backup db-restore

# Repertoire de destination des sauvegardes: make db-backup BACKUP_DIR=D:/backups
BACKUP_DIR ?= backups
# Archive a restaurer: make db-restore ARCHIVE=backups/2026-01-01_10-00-00-data_postgres_data.tar.gz
ARCHIVE ?=

help:
	@echo "Available targets:"
	@echo "  android-release-builder  Build and run Android release builder container"
	@echo "  restart-services         Restart all running docker compose services"
	@echo "  data-mkdocs-build        Build MkDocs documentation"
	@echo "  db-backup                Stop postgres and archive its volume (BACKUP_DIR=...)"
	@echo "  db-restore               Restore postgres volume from an archive (ARCHIVE=...)"

android-release-builder:
	docker compose --profile android-build up --build android-release-builder

# Restart all services and force rebuild the images
restart-services:
	docker compose -f 'docker-compose.yml' up -d --build --force-recreate

data-mkdocs-build:
	docker compose exec -T data-mkdocs mkdocs build --clean

# Sauvegarde a froid du volume PostgreSQL
db-backup:
	pwsh -NoProfile -File scripts/database/Backup-Database.ps1 -BackupDirectory "$(BACKUP_DIR)"

# Restauration du volume PostgreSQL depuis une archive
db-restore:
	@if [ -z "$(ARCHIVE)" ]; then echo "ARCHIVE is required: make db-restore ARCHIVE=path/to/archive.tar.gz"; exit 1; fi
	pwsh -NoProfile -File scripts/database/Restore-Database.ps1 -ArchivePath "$(ARCHIVE)" -Force