package fr.tiogars.data.dev.docs.section.services;

import java.util.Optional;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.dev.docs.section.entities.SectionEntity;
import fr.tiogars.data.dev.docs.section.models.Section;
import fr.tiogars.data.dev.docs.section.repositories.SectionRepository;

@Service
public class SectionGetOneService {
    private final SectionRepository sectionRepository;

    public SectionGetOneService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section getSectionById(String id) {
        Optional<SectionEntity> sectionEntityOpt = sectionRepository.findById(id);
        SectionEntity sectionEntity = sectionEntityOpt.orElseThrow(() -> new DataNotFoundException("Section non trouvée pour l'id: " + id));
        Section section = new Section();
        section.setId(sectionEntity.getId());
        section.setName(sectionEntity.getName());
        section.setDescription(sectionEntity.getDescription());
        return section;
    }
}
