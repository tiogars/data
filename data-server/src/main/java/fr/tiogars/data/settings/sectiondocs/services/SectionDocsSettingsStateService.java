package fr.tiogars.data.settings.sectiondocs.services;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.docs.section.services.SectionDocsFilesystemSyncService;
import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.repositories.SectionRepository;
import fr.tiogars.data.settings.sectiondocs.entities.SectionDocsSettingEntity;
import fr.tiogars.data.settings.sectiondocs.models.SectionDocsSetting;
import fr.tiogars.data.settings.sectiondocs.models.SectionDocsSettingsState;
import fr.tiogars.data.settings.sectiondocs.repositories.SectionDocsSettingRepository;

@Service
public class SectionDocsSettingsStateService {

    private final SectionDocsSettingRepository sectionDocsSettingRepository;
    private final SectionRepository sectionRepository;
    private final SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService;

    public SectionDocsSettingsStateService(
        SectionDocsSettingRepository sectionDocsSettingRepository,
        SectionRepository sectionRepository,
        SectionDocsFilesystemSyncService sectionDocsFilesystemSyncService
    ) {
        this.sectionDocsSettingRepository = sectionDocsSettingRepository;
        this.sectionRepository = sectionRepository;
        this.sectionDocsFilesystemSyncService = sectionDocsFilesystemSyncService;
    }

    public SectionDocsSettingsState getState() {
        List<SectionDocsSetting> items = sectionDocsSettingRepository.findAll().stream()
            .map(SectionDocsSettingsModelMapper::toModel)
            .toList();

        return new SectionDocsSettingsState(items);
    }

    @Transactional
    public SectionDocsSettingsState replaceState(SectionDocsSettingsState state) {
        List<SectionDocsSetting> items = state != null && state.getItems() != null
            ? new ArrayList<>(state.getItems())
            : List.of();
        var previousSnapshots = sectionDocsFilesystemSyncService.captureConfiguredRootSnapshots();

        validateItems(items);

        sectionDocsSettingRepository.deleteAllInBatch();

        List<SectionDocsSettingEntity> entities = items.stream()
            .map(SectionDocsSettingsModelMapper::toEntity)
            .toList();

        sectionDocsSettingRepository.saveAll(entities);
        sectionDocsFilesystemSyncService.syncAfterSettingsUpdated(previousSnapshots);
        return getState();
    }

    private void validateItems(List<SectionDocsSetting> items) {
        Set<String> seenSectionIds = new LinkedHashSet<>();

        for (SectionDocsSetting item : items) {
            String sectionId = requireText(item.getSectionId(), "L'identifiant de section est obligatoire.");
            String storagePath = normalizeStoragePath(item.getStoragePath());

            if (!seenSectionIds.add(sectionId)) {
                throw new IllegalArgumentException("Une seule configuration est autorisée par section racine.");
            }

            SectionEntity section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new DataNotFoundException("Section introuvable pour l'id: " + sectionId));

            if (section.getParent() != null) {
                throw new IllegalArgumentException("Seules les sections racines peuvent être configurées.");
            }

            item.setSectionId(sectionId);
            item.setStoragePath(storagePath);
        }
    }

    private String normalizeStoragePath(String storagePath) {
        String normalized = requireText(storagePath, "Le chemin de stockage est obligatoire.")
            .replace('\\', '/');

        if (normalized.startsWith("/") || normalized.matches("^[A-Za-z]:.*")) {
            throw new IllegalArgumentException("Le chemin doit rester relatif à volumes/docs.");
        }

        String[] segments = normalized.split("/");
        List<String> cleanedSegments = new ArrayList<>();
        for (String segment : segments) {
            if (segment == null || segment.isBlank()) {
                continue;
            }
            if ("..".equals(segment.trim())) {
                throw new IllegalArgumentException("Le chemin ne peut pas sortir de volumes/docs.");
            }
            cleanedSegments.add(segment.trim());
        }

        if (cleanedSegments.isEmpty()) {
            throw new IllegalArgumentException("Le chemin de stockage est obligatoire.");
        }

        return String.join("/", cleanedSegments);
    }

    private String requireText(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(message);
        }
        return value.trim();
    }
}