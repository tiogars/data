package fr.tiogars.data.docs.section.services;

import java.util.List;

import org.springframework.stereotype.Service;

import fr.tiogars.data.docs.section.entities.SectionEntity;
import fr.tiogars.data.docs.section.models.SectionListResponse;
import fr.tiogars.data.docs.section.repositories.SectionRepository;

@Service
public class SectionListService {
    
    private final SectionRepository sectionRepository;

    public SectionListService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public SectionListResponse listSections() {
        List<SectionEntity> sectionEntities = sectionRepository.findAll();
        return new SectionListResponse(SectionModelMapper.toSectionTree(sectionEntities), sectionEntities.size());
    }
}
