package fr.tiogars.data.cave.couleur.services;

import org.springframework.stereotype.Service;
import fr.tiogars.data.cave.couleur.repositories.CouleurRepository;
import fr.tiogars.data.cave.couleur.models.Couleur;

@Service
public class CouleurGetOneService {
    private final CouleurRepository couleurRepository;
    public CouleurGetOneService(CouleurRepository couleurRepository) { this.couleurRepository = couleurRepository; }
    public Couleur getCouleur(String id) { return couleurRepository.findById(id).map(CouleurModelMapper::toModel).orElseThrow(() -> new fr.tiogars.data.common.exceptions.DataNotFoundException("Couleur non trouvee pour l'id: " + id)); }
}
