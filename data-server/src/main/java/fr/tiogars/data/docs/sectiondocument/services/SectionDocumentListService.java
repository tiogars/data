package fr.tiogars.data.docs.sectiondocument.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.docs.sectiondocument.models.SectionDocumentListResponse;
import fr.tiogars.data.docs.sectiondocument.repositories.SectionDocumentRepository;

@Service
public class SectionDocumentListService {

    private final SectionDocumentRepository sectionDocumentRepository;

    public SectionDocumentListService(SectionDocumentRepository sectionDocumentRepository) {
        this.sectionDocumentRepository = sectionDocumentRepository;
    }

    public SectionDocumentListResponse listDocuments() {
        var entities = sectionDocumentRepository.findAll();
        var items = entities.stream().map(SectionDocumentModelMapper::toModel).toList();
        return new SectionDocumentListResponse(items, items.size());
    }
}
