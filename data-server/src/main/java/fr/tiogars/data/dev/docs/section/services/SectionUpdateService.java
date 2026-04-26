package fr.tiogars.data.dev.docs.section.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.section.entities.SectionEntity;
import fr.tiogars.data.dev.docs.section.models.Section;
import fr.tiogars.data.dev.docs.section.repositories.SectionRepository;

@Service
public class SectionUpdateService {
    private final SectionRepository sectionRepository;

    public SectionUpdateService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section updateSection(String id, Section sectionUpdate) {
        SectionEntity sectionEntity = sectionRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Section non trouvée pour l'id: " + id));
        sectionEntity.setName(sectionUpdate.getName());
        sectionEntity.setDescription(sectionUpdate.getDescription());
        SectionEntity updatedEntity = sectionRepository.save(sectionEntity);
        Section updatedSection = new Section();
        updatedSection.setId(updatedEntity.getId());
        updatedSection.setName(updatedEntity.getName());
        updatedSection.setDescription(updatedEntity.getDescription());
        return updatedSection;
    }
}
