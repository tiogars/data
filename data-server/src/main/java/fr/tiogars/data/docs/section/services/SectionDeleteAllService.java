package fr.tiogars.data.docs.section.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.docs.section.repositories.SectionRepository;

/**
 * Service pour supprimer toutes les sections de l'application.
 * Cette opération est irréversible et doit être utilisée avec précaution.
 */
@Service
public class SectionDeleteAllService {

    private final SectionRepository sectionRepository;

    public SectionDeleteAllService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    /**
     * Supprime toutes les sections de l'application.
     * Cette opération est irréversible et doit être utilisée avec précaution.
     */
    public void deleteAllSections() {
        sectionRepository.deleteAll();
    }
    
}