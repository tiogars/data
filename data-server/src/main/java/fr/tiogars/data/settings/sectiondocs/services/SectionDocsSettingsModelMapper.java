package fr.tiogars.data.settings.sectiondocs.services;

import fr.tiogars.data.settings.sectiondocs.entities.SectionDocsSettingEntity;
import fr.tiogars.data.settings.sectiondocs.models.SectionDocsSetting;

final class SectionDocsSettingsModelMapper {

    private SectionDocsSettingsModelMapper() {
    }

    static SectionDocsSetting toModel(SectionDocsSettingEntity entity) {
        SectionDocsSetting model = new SectionDocsSetting();
        model.setId(entity.getId());
        model.setSectionId(entity.getSectionId());
        model.setStoragePath(entity.getStoragePath());
        return model;
    }

    static SectionDocsSettingEntity toEntity(SectionDocsSetting model) {
        SectionDocsSettingEntity entity = new SectionDocsSettingEntity();
        entity.setId(model.getId());
        entity.setSectionId(model.getSectionId());
        entity.setStoragePath(model.getStoragePath());
        return entity;
    }
}