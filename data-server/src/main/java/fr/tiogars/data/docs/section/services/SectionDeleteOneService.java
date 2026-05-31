package fr.tiogars.data.docs.section.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.repositories.SectionRepository;

/**
 * Service pour supprimer une section précise de l'application.
 * Cette opération est irréversible et doit être utilisée avec précaution.
 */
@Service
public class SectionDeleteOneService {

    private final SectionRepository sectionRepository;

    public SectionDeleteOneService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    /**
     * Supprime une section par son identifiant (ID).
     * @param sectionId l'identifiant de la section à supprimer
     */
    public void deleteSectionById(String sectionId) {
        if (!sectionRepository.existsById(sectionId)) {
            throw new DataNotFoundException("Section non trouvée avec l'identifiant : " + sectionId);
        }

        List<SectionEntity> sections = sectionRepository.findAll();
        Map<String, List<String>> childrenByParentId = new HashMap<>();

        for (SectionEntity section : sections) {
            if (section.getParent() == null) {
                continue;
            }

            childrenByParentId
                .computeIfAbsent(section.getParent().getId(), key -> new ArrayList<>())
                .add(section.getId());
        }

        sectionRepository.deleteAllById(collectSectionIdsToDelete(sectionId, childrenByParentId));
    }

    private List<String> collectSectionIdsToDelete(String sectionId, Map<String, List<String>> childrenByParentId) {
        List<String> ids = new ArrayList<>();

        for (String childId : childrenByParentId.getOrDefault(sectionId, List.of())) {
            ids.addAll(collectSectionIdsToDelete(childId, childrenByParentId));
        }

        ids.add(sectionId);

        return ids;
    }
}
