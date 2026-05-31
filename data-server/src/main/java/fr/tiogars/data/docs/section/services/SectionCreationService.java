package fr.tiogars.data.docs.section.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.forms.SectionCreationForm;
import fr.tiogars.data.docs.section.models.Section;
import fr.tiogars.data.docs.section.repositories.SectionRepository;

@Service
public class SectionCreationService {
    
    private final SectionRepository sectionRepository;

    public SectionCreationService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section createSection(SectionCreationForm sectionCreationForm) {
        SectionEntity section = new SectionEntity();
        section.setName(sectionCreationForm.getName());
        section.setDescription(sectionCreationForm.getDescription());
        section.setParent(resolveParent(sectionCreationForm.getParentId()));

        if (sectionRepository.findByName(section.getName()).isPresent()) {
            throw new IllegalArgumentException("Une section avec ce nom existe déjà.");
        }

        SectionEntity createdSectionEntity = sectionRepository.save(section);

        return SectionModelMapper.toSectionModel(createdSectionEntity);
    }

    private SectionEntity resolveParent(String parentId) {
        if (parentId == null || parentId.isBlank()) {
            return null;
        }

        return sectionRepository.findById(parentId)
            .orElseThrow(() -> new DataNotFoundException("Section parente non trouvée pour l'id: " + parentId));
    }
}
