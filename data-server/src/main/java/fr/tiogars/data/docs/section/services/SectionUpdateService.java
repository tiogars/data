package fr.tiogars.data.docs.section.services;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.models.Section;
import fr.tiogars.data.docs.section.repositories.SectionRepository;
import fr.tiogars.data.docs.sectiondocument.entities.SectionDocumentEntity;
import fr.tiogars.data.docs.sectiondocument.repositories.SectionDocumentRepository;

@Service
public class SectionUpdateService {
    private final SectionRepository sectionRepository;
    private final SectionDocumentRepository sectionDocumentRepository;
    private final SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService;

    public SectionUpdateService(
        SectionRepository sectionRepository,
        SectionDocumentRepository sectionDocumentRepository,
        SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService
    ) {
        this.sectionRepository = sectionRepository;
        this.sectionDocumentRepository = sectionDocumentRepository;
        this.sectionDocsFilesystemSyncService = sectionDocsFilesystemSyncService;
    }

    @Transactional
    public Section updateSection(String id, Section sectionUpdate) {
        SectionEntity sectionEntity = sectionRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Section non trouvée pour l'id: " + id));
        SectionDocsFilesystemSyncService.SectionDocsSyncSnapshot previousSnapshot = sectionDocsFilesystemSyncService.captureSnapshot(id);

        List<SectionEntity> allSections = sectionRepository.findAll();
        Map<String, SectionEntity> sectionsById = new HashMap<>();
        for (SectionEntity section : allSections) {
            sectionsById.put(section.getId(), section);
        }

        String targetDocumentId = resolveDocumentId(sectionEntity, sectionUpdate.getDocumentId());

        sectionEntity.setName(sectionUpdate.getName());
        sectionEntity.setDescription(sectionUpdate.getDescription());
        sectionEntity.setDisplayOrder(normalizeDisplayOrder(sectionUpdate.getDisplayOrder()));
        sectionEntity.setDocument(resolveDocument(targetDocumentId));
        sectionEntity.setParent(resolveParent(id, sectionUpdate.getParentId(), sectionsById, targetDocumentId));

        SectionEntity updatedEntity = sectionRepository.save(sectionEntity);
        sectionDocsFilesystemSyncService.syncAfterSectionUpdated(updatedEntity.getId(), previousSnapshot);
        return SectionModelMapper.toSectionModel(updatedEntity);
    }

    private String resolveDocumentId(SectionEntity currentSection, String requestedDocumentId) {
        if (requestedDocumentId == null || requestedDocumentId.isBlank()) {
            if (currentSection.getDocument() == null || currentSection.getDocument().getId() == null) {
                throw new IllegalArgumentException("Le document est requis.");
            }
            return currentSection.getDocument().getId();
        }

        if (currentSection.getDocument() != null
            && currentSection.getDocument().getId() != null
            && !requestedDocumentId.equals(currentSection.getDocument().getId())) {
            throw new IllegalArgumentException("Le changement de document d'une section existante n'est pas autorisé.");
        }

        return requestedDocumentId;
    }

    private SectionDocumentEntity resolveDocument(String documentId) {
        return sectionDocumentRepository.findById(documentId)
            .orElseThrow(() -> new DataNotFoundException("Document non trouvé pour l'id: " + documentId));
    }

    private SectionEntity resolveParent(String sectionId, String parentId, Map<String, SectionEntity> sectionsById, String documentId) {
        if (parentId == null || parentId.isBlank()) {
            return null;
        }

        if (sectionId.equals(parentId)) {
            throw new IllegalArgumentException("Une section ne peut pas être sa propre parente.");
        }

        SectionEntity parent = sectionsById.get(parentId);

        if (parent == null) {
            throw new DataNotFoundException("Section parente non trouvée pour l'id: " + parentId);
        }

        if (parent.getDocument() == null || !documentId.equals(parent.getDocument().getId())) {
            throw new IllegalArgumentException("La section parente doit appartenir au même document.");
        }

        SectionEntity current = parent;
        while (current != null) {
            if (sectionId.equals(current.getId())) {
                throw new IllegalArgumentException("Une section ne peut pas être déplacée sous une de ses sous-sections.");
            }
            current = current.getParent();
        }

        return parent;
    }

    private Integer normalizeDisplayOrder(Integer displayOrder) {
        if (displayOrder == null) {
            return 0;
        }

        if (displayOrder < 0) {
            throw new IllegalArgumentException("L'ordre d'affichage doit être positif ou nul.");
        }

        return displayOrder;
    }
}
