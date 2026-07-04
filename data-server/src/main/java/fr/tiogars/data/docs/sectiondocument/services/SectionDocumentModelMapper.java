package fr.tiogars.data.docs.sectiondocument.services;

import fr.tiogars.data.docs.sectiondocument.entities.SectionDocumentEntity;
import fr.tiogars.data.docs.sectiondocument.models.SectionDocument;

final class SectionDocumentModelMapper {

    private SectionDocumentModelMapper() {
    }

    static SectionDocument toModel(SectionDocumentEntity entity) {
        SectionDocument model = new SectionDocument();
        model.setId(entity.getId());
        model.setName(entity.getName());
        model.setStoragePath(entity.getStoragePath());
        return model;
    }
}
