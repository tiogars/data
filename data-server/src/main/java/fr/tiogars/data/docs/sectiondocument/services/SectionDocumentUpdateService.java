package fr.tiogars.data.docs.sectiondocument.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.docs.section.services.SectionDocsFilesystemSyncService;
import fr.tiogars.data.docs.sectiondocument.entities.SectionDocumentEntity;
import fr.tiogars.data.docs.sectiondocument.models.SectionDocument;
import fr.tiogars.data.docs.sectiondocument.repositories.SectionDocumentRepository;

@Service
public class SectionDocumentUpdateService {

    private final SectionDocumentRepository sectionDocumentRepository;
    private final SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService;

    public SectionDocumentUpdateService(
        SectionDocumentRepository sectionDocumentRepository,
        SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService
    ) {
        this.sectionDocumentRepository = sectionDocumentRepository;
        this.sectionDocsFilesystemSyncService = sectionDocsFilesystemSyncService;
    }

    @Transactional
    public SectionDocument update(String id, SectionDocument request) {
        if (request == null) {
            throw new IllegalArgumentException("Le document est requis.");
        }

        SectionDocumentEntity entity = sectionDocumentRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Document non trouvé pour l'id: " + id));

        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Le nom du document est requis.");
        }

        String normalizedName = request.getName().trim();
        sectionDocumentRepository.findByNameIgnoreCase(normalizedName)
            .filter(found -> !found.getId().equals(id))
            .ifPresent(found -> {
                throw new IllegalArgumentException("Un document avec ce nom existe déjà.");
            });

        String normalizedStoragePath = normalizeStoragePath(request.getStoragePath());

        entity.setName(normalizedName);
        entity.setStoragePath(normalizedStoragePath);
        SectionDocument updated = SectionDocumentModelMapper.toModel(sectionDocumentRepository.save(entity));

        sectionDocsFilesystemSyncService.syncDocumentById(id);

        return updated;
    }

    private String normalizeStoragePath(String storagePath) {
        if (storagePath == null || storagePath.isBlank()) {
            throw new IllegalArgumentException("Le chemin relatif du document est requis.");
        }

        String normalized = storagePath.trim().replace('\\', '/');
        while (normalized.startsWith("/")) {
            normalized = normalized.substring(1);
        }
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }

        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Le chemin relatif du document est invalide.");
        }

        return normalized;
    }
}
