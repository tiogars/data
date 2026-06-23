package fr.tiogars.data.cave.maison.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.normalizeNullableText;
import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.maison.entities.MaisonEntity;
import fr.tiogars.data.cave.maison.forms.MaisonCreationForm;
import fr.tiogars.data.cave.maison.models.Maison;
import fr.tiogars.data.cave.maison.repositories.MaisonRepository;

@Service
public class MaisonCreationService { private final MaisonRepository maisonRepository; public MaisonCreationService(MaisonRepository maisonRepository) { this.maisonRepository = maisonRepository; } public Maison createMaison(MaisonCreationForm form) { validateUniqueName(form.getName(), null); MaisonEntity entity = new MaisonEntity(); applyValues(entity, form.getName(), form.getWebsite()); return MaisonModelMapper.toModel(maisonRepository.save(entity)); } static void applyValues(MaisonEntity entity, String name, String website) { entity.setName(requireText(name, "Le nom de la maison est obligatoire.")); entity.setWebsite(normalizeNullableText(website)); } void validateUniqueName(String name, String currentId) { maisonRepository.findByName(requireText(name, "Le nom de la maison est obligatoire.")).filter(entity -> !entity.getId().equals(currentId)).ifPresent(entity -> { throw new IllegalArgumentException("Une maison avec ce nom existe deja."); }); } }
