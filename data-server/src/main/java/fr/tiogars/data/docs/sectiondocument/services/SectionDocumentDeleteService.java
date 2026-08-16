package fr.tiogars.data.docs.sectiondocument.services;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.docs.section.repositories.SectionRepository;
import fr.tiogars.data.docs.sectiondocument.repositories.SectionDocumentRepository;

@Service
public class SectionDocumentDeleteService {

    private final SectionDocumentRepository sectionDocumentRepository;
    private final SectionRepository sectionRepository;

    public SectionDocumentDeleteService(
        SectionDocumentRepository sectionDocumentRepository,
        SectionRepository sectionRepository
    ) {
        this.sectionDocumentRepository = sectionDocumentRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public void deleteById(String id) {
        if (!sectionDocumentRepository.existsById(id)) {
            throw new DataNotFoundException("Document non trouvé pour l'id: " + id);
        }

        if (!sectionRepository.findAllByDocument_Id(id, SectionRepository.DEFAULT_SECTION_SORT).isEmpty()) {
            throw new IllegalArgumentException("Ce document contient encore des sections. Supprimez-les ou déplacez-les avant de supprimer le document.");
        }

        sectionDocumentRepository.deleteById(id);
    }
}
