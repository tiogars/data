package fr.tiogars.data.dev.docs.section.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.dev.docs.section.entities.SectionEntity;
import fr.tiogars.data.dev.docs.section.forms.SectionCreationForm;
import fr.tiogars.data.dev.docs.section.models.Section;
import fr.tiogars.data.dev.docs.section.repositories.SectionRepository;

@Service
public class SectionCreationService {
    
    private final SectionRepository sectionRepository;

    public SectionCreationService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    public Section createSection(SectionCreationForm sectionCreationForm) {
        
        // Convertir le formulaire en entité
        SectionEntity section = new SectionEntity();
        section.setName(sectionCreationForm.getName());
        section.setDescription(sectionCreationForm.getDescription());

        // Rechercher si une section avec le même nom existe déjà
        if (sectionRepository.findByName(section.getName()).isPresent()) {
            throw new IllegalArgumentException("Une section avec ce nom existe déjà.");
        }

        // Enregistrer la nouvelle section dans la base de données
        SectionEntity createdSectionEntity = sectionRepository.save(section);

        // Convertir l'entité créée en modèle et la retourner
        Section createdSection = new Section();
        createdSection.setId(createdSectionEntity.getId());
        createdSection.setName(createdSectionEntity.getName());
        createdSection.setDescription(createdSectionEntity.getDescription());

        return createdSection;
    }
}
