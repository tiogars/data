package fr.tiogars.data.dev.docs.section.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.section.repositories.SectionRepository;
import fr.tiogars.data.common.exceptions.DataNotFoundException;

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
        sectionRepository.deleteById(sectionId);
    }
}
