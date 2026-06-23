package fr.tiogars.data.cave.couleur.services;

import static fr.tiogars.data.common.validation.TextValidationUtils.requireText;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.couleur.entities.CouleurEntity;
import fr.tiogars.data.cave.couleur.forms.CouleurCreationForm;
import fr.tiogars.data.cave.couleur.models.Couleur;
import fr.tiogars.data.cave.couleur.repositories.CouleurRepository;

@Service
public class CouleurCreationService {
    private final CouleurRepository couleurRepository;
    public CouleurCreationService(CouleurRepository couleurRepository) { this.couleurRepository = couleurRepository; }
    public Couleur createCouleur(CouleurCreationForm form) {
        validateUniqueName(form.getName(), null);
        CouleurEntity entity = new CouleurEntity();
        applyValues(entity, form.getName());
        return CouleurModelMapper.toModel(couleurRepository.save(entity));
    }
    static void applyValues(CouleurEntity entity, String name) { entity.setName(requireText(name, "Le nom de la couleur est obligatoire.")); }
    void validateUniqueName(String name, String currentId) {
        couleurRepository.findByName(requireText(name, "Le nom de la couleur est obligatoire."))
            .filter(entity -> !entity.getId().equals(currentId))
            .ifPresent(entity -> { throw new IllegalArgumentException("Une couleur avec ce nom existe deja."); });
    }
}
