package fr.tiogars.data.cave.couleur.services;

import org.springframework.stereotype.Service;

import fr.tiogars.data.common.exceptions.DataNotFoundException;
import fr.tiogars.data.cave.couleur.entities.CouleurEntity;
import fr.tiogars.data.cave.couleur.models.Couleur;
import fr.tiogars.data.cave.couleur.repositories.CouleurRepository;

@Service
public class CouleurUpdateService {
    private final CouleurRepository couleurRepository;
    private final CouleurCreationService couleurCreationService;
    public CouleurUpdateService(CouleurRepository couleurRepository, CouleurCreationService couleurCreationService) { this.couleurRepository = couleurRepository; this.couleurCreationService = couleurCreationService; }
    public Couleur updateCouleur(String id, Couleur couleur) {
        CouleurEntity entity = couleurRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Couleur non trouvee pour l'id: " + id));
        couleurCreationService.validateUniqueName(couleur.getName(), id);
        CouleurCreationService.applyValues(entity, couleur.getName());
        return CouleurModelMapper.toModel(couleurRepository.save(entity));
    }
}
