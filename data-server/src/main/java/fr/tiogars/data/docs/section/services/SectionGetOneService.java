package fr.tiogars.data.docs.section.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.models.Section;
import fr.tiogars.data.docs.section.repositories.SectionRepository;

@Service
public class SectionGetOneService {
    private final SectionRepository sectionRepository;

    public SectionGetOneService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section getSectionById(String id) {
        SectionEntity sectionEntity = sectionRepository.findById(id)
            .orElseThrow(() -> new DataNotFoundException("Section non trouvée pour l'id: " + id));

        List<SectionEntity> sectionEntities = sectionRepository.findAll();
        Map<String, Section> sectionsById = SectionModelMapper.toSectionMap(sectionEntities);
        Section section = sectionsById.get(sectionEntity.getId());

        if (section == null) {
            throw new DataNotFoundException("Section non trouvée pour l'id: " + id);
        }

        return section;
    }
}
