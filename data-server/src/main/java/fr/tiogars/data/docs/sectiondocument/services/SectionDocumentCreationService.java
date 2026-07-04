package fr.tiogars.data.docs.sectiondocument.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.docs.sectiondocument.entities.SectionDocumentEntity;
import fr.tiogars.data.docs.sectiondocument.models.SectionDocument;
import fr.tiogars.data.docs.sectiondocument.repositories.SectionDocumentRepository;

@Service
public class SectionDocumentCreationService {

    private final SectionDocumentRepository sectionDocumentRepository;

    public SectionDocumentCreationService(SectionDocumentRepository sectionDocumentRepository) {
        this.sectionDocumentRepository = sectionDocumentRepository;
    }

    @Transactional
    public SectionDocument create(SectionDocument request) {
        validateRequest(request);

        if (sectionDocumentRepository.existsByNameIgnoreCase(request.getName().trim())) {
            throw new IllegalArgumentException("Un document avec ce nom existe déjà.");
        }

        SectionDocumentEntity entity = new SectionDocumentEntity();
        entity.setName(request.getName().trim());
        entity.setStoragePath(normalizeStoragePath(request.getStoragePath()));

        return SectionDocumentModelMapper.toModel(sectionDocumentRepository.save(entity));
    }

    private void validateRequest(SectionDocument request) {
        if (request == null) {
            throw new IllegalArgumentException("Le document est requis.");
        }

        if (request.getName() == null || request.getName().isBlank()) {
            throw new IllegalArgumentException("Le nom du document est requis.");
        }

        if (request.getStoragePath() == null || request.getStoragePath().isBlank()) {
            throw new IllegalArgumentException("Le chemin relatif du document est requis.");
        }
    }

    private String normalizeStoragePath(String storagePath) {
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
