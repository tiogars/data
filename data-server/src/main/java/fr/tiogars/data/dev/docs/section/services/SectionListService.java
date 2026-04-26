package fr.tiogars.data.dev.docs.section.services;


import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.section.entities.SectionEntity;
import fr.tiogars.data.dev.docs.section.models.Section;
import fr.tiogars.data.dev.docs.section.models.SectionListResponse;
import fr.tiogars.data.dev.docs.section.repositories.SectionRepository;

@Service
public class SectionListService {
    
    private final SectionRepository sectionRepository;

    public SectionListService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public SectionListResponse listSections() {
        List<Section> sections = sectionRepository.findAll().stream()
            .map(this::toSectionModel)
            .toList();
        return new SectionListResponse(sections);
    }

    private Section toSectionModel(SectionEntity sectionEntity) {
        Section section = new Section();
        section.setId(sectionEntity.getId());
        section.setName(sectionEntity.getName());
        section.setDescription(sectionEntity.getDescription());
        return section;
    }
}
