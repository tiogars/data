package fr.tiogars.data.docs.section.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.models.Section;

final class SectionModelMapper {

    private SectionModelMapper() {
    }

    static Section toSectionModel(SectionEntity sectionEntity) {
        Section section = new Section();
        section.setId(sectionEntity.getId());
        section.setName(sectionEntity.getName());
        section.setDescription(sectionEntity.getDescription());
        section.setDisplayOrder(sectionEntity.getDisplayOrder());
        section.setParentId(sectionEntity.getParent() != null ? sectionEntity.getParent().getId() : null);
        section.setDocumentId(sectionEntity.getDocument() != null ? sectionEntity.getDocument().getId() : null);
        section.setChildren(new ArrayList<>());
        return section;
    }

    static List<Section> toSectionTree(List<SectionEntity> sectionEntities) {
        Map<String, Section> sectionsById = new LinkedHashMap<>();

        for (SectionEntity entity : sectionEntities) {
            sectionsById.put(entity.getId(), toSectionModel(entity));
        }

        List<Section> roots = new ArrayList<>();

        for (SectionEntity entity : sectionEntities) {
            Section section = sectionsById.get(entity.getId());
            String parentId = entity.getParent() != null ? entity.getParent().getId() : null;

            if (parentId != null && sectionsById.containsKey(parentId)) {
                sectionsById.get(parentId).getChildren().add(section);
                continue;
            }

            roots.add(section);
        }

        return roots;
    }

    static Map<String, Section> toSectionMap(List<SectionEntity> sectionEntities) {
        Map<String, Section> sectionsById = new LinkedHashMap<>();

        for (SectionEntity entity : sectionEntities) {
            sectionsById.put(entity.getId(), toSectionModel(entity));
        }

        for (SectionEntity entity : sectionEntities) {
            Section section = sectionsById.get(entity.getId());
            String parentId = entity.getParent() != null ? entity.getParent().getId() : null;

            if (parentId != null && sectionsById.containsKey(parentId)) {
                sectionsById.get(parentId).getChildren().add(section);
            }
        }

        return sectionsById;
    }
}