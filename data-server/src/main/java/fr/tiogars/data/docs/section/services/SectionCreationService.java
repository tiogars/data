package fr.tiogars.data.docs.section.services;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.forms.SectionCreationForm;
import fr.tiogars.data.docs.section.models.Section;
import fr.tiogars.data.docs.section.repositories.SectionRepository;
import fr.tiogars.data.docs.sectiondocument.entities.SectionDocumentEntity;
import fr.tiogars.data.docs.sectiondocument.repositories.SectionDocumentRepository;

@Service
public class SectionCreationService {
    
    private final SectionRepository sectionRepository;
    private final SectionDocumentRepository sectionDocumentRepository;
    private final SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService;

    public SectionCreationService(
        SectionRepository sectionRepository,
        SectionDocumentRepository sectionDocumentRepository,
        SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService
    ) {
        this.sectionRepository = sectionRepository;
        this.sectionDocumentRepository = sectionDocumentRepository;
        this.sectionDocsFilesystemSyncService = sectionDocsFilesystemSyncService;
    }

    @Transactional
    public Section createSection(SectionCreationForm sectionCreationForm) {
        SectionEntity section = new SectionEntity();
        SectionDocumentEntity document = resolveDocument(sectionCreationForm.getDocumentId());
        section.setName(sectionCreationForm.getName());
        section.setDescription(sectionCreationForm.getDescription());
        section.setDisplayOrder(normalizeDisplayOrder(sectionCreationForm.getDisplayOrder()));
        section.setDocument(document);
        section.setParent(resolveParent(sectionCreationForm.getParentId(), document.getId()));

        if (sectionRepository.findByNameAndDocument_Id(section.getName(), document.getId()).isPresent()) {
            throw new IllegalArgumentException("Une section avec ce nom existe déjà.");
        }

        SectionEntity createdSectionEntity = sectionRepository.save(section);
        sectionDocsFilesystemSyncService.syncAfterSectionCreated(createdSectionEntity.getId());

        return SectionModelMapper.toSectionModel(createdSectionEntity);
    }

    private SectionDocumentEntity resolveDocument(String documentId) {
        if (documentId == null || documentId.isBlank()) {
            throw new IllegalArgumentException("Le document est requis pour créer une section.");
        }

        return sectionDocumentRepository.findById(documentId)
            .orElseThrow(() -> new DataNotFoundException("Document non trouvé pour l'id: " + documentId));
    }

    private SectionEntity resolveParent(String parentId, String documentId) {
        if (parentId == null || parentId.isBlank()) {
            return null;
        }

        SectionEntity parent = sectionRepository.findById(parentId)
            .orElseThrow(() -> new DataNotFoundException("Section parente non trouvée pour l'id: " + parentId));

        if (parent.getDocument() == null || !documentId.equals(parent.getDocument().getId())) {
            throw new IllegalArgumentException("La section parente doit appartenir au même document.");
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
