package fr.tiogars.data.cave.contenant.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.contenant.entities.ContenantEntity;
import fr.tiogars.data.cave.contenant.forms.ContenantCreationForm;
import fr.tiogars.data.cave.contenant.models.Contenant;
import fr.tiogars.data.cave.contenant.repositories.ContenantRepository;

@Service
public class ContenantCreationService { private final ContenantRepository contenantRepository; public ContenantCreationService(ContenantRepository contenantRepository) { this.contenantRepository = contenantRepository; } public Contenant createContenant(ContenantCreationForm form) { validateUniqueName(form.getName(), null); ContenantEntity entity = new ContenantEntity(); applyValues(entity, form.getName(), form.getVolumeCl()); return ContenantModelMapper.toModel(contenantRepository.save(entity)); } static void applyValues(ContenantEntity entity, String name, Integer volumeCl) { entity.setName(requireText(name, "Le nom du contenant est obligatoire.")); entity.setVolumeCl(volumeCl); } void validateUniqueName(String name, String currentId) { contenantRepository.findByName(requireText(name, "Le nom du contenant est obligatoire.")).filter(entity -> !entity.getId().equals(currentId)).ifPresent(entity -> { throw new IllegalArgumentException("Un contenant avec ce nom existe deja."); }); } }
