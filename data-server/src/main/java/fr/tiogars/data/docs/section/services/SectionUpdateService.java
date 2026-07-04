package fr.tiogars.data.docs.section.services;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.models.Section;
import fr.tiogars.data.docs.section.repositories.SectionRepository;
import fr.tiogars.data.settings.sectiondocs.repositories.SectionDocsSettingRepository;

@Service
public class SectionUpdateService {
    private final SectionRepository sectionRepository;
    private final SectionDocsSettingRepository sectionDocsSettingRepository;
    private final SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService;

    public SectionUpdateService(
        SectionRepository sectionRepository,
        SectionDocsSettingRepository sectionDocsSettingRepository,
        SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService
    ) {
        this.sectionRepository = sectionRepository;
        this.sectionDocsSettingRepository = sectionDocsSettingRepository;
        this.sectionDocsFilesystemSyncService = sectionDocsFilesystemSyncService;
    }

    @Transactional
    public Section updateSection(String id, Section sectionUpdate) {
        SectionEntity sectionEntity = sectionRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Section non trouvée pour l'id: " + id));
        SectionDocsFilesystemSyncService.SectionDocsSyncSnapshot previousSnapshot = sectionDocsFilesystemSyncService.captureSnapshot(id);

        List<SectionEntity> allSections = sectionRepository.findAll();
        Map<String, SectionEntity> sectionsById = allSections.stream()
            .collect(Collectors.toMap(SectionEntity::getId, Function.identity()));

        if (sectionUpdate.getParentId() != null && !sectionUpdate.getParentId().isBlank() && sectionDocsSettingRepository.existsBySectionId(id)) {
            throw new IllegalArgumentException("Une section racine configurée ne peut pas devenir une sous-section tant que ses paramètres docs existent.");
        }

        sectionEntity.setName(sectionUpdate.getName());
        sectionEntity.setDescription(sectionUpdate.getDescription());
        sectionEntity.setDisplayOrder(normalizeDisplayOrder(sectionUpdate.getDisplayOrder()));
        sectionEntity.setParent(resolveParent(id, sectionUpdate.getParentId(), sectionsById));

        SectionEntity updatedEntity = sectionRepository.save(sectionEntity);
        sectionDocsFilesystemSyncService.syncAfterSectionUpdated(updatedEntity.getId(), previousSnapshot);
        return SectionModelMapper.toSectionModel(updatedEntity);
    }

    private SectionEntity resolveParent(String sectionId, String parentId, Map<String, SectionEntity> sectionsById) {
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
